package dk.mustache.corelib.validation.views

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children

interface ValidatorView {
    fun listenOnChildValidationViewValidationStates(
        validationViews: List<ValidationView>,
        updateValidState: (Boolean) -> Unit
    ) {
        validationViews.forEach {
            it.addOnValidationChangedListener {
                val newValidState = validationViews.all { it.getViewIsValid() ?: false }
                updateValidState(newValidState)
            }
        }

        // Initialize View State
        val newValidState = validationViews.all { it.getViewIsValid() ?: false }
        updateValidState(newValidState)
    }

    fun getAllChildValidationViews(parentView: ViewGroup): List<ValidationView> {
        val children = getAllChildrenControls(parentView)
        return children.mapNotNull {
            if (it is ValidationView) {
                it
            } else {
                null
            }
        }
    }

    private fun getAllChildrenControls(parentView: ViewGroup): List<View> {
        val rawChildren = parentView.children.toList()
        val controlViews = mutableListOf<View>()
        val grandChildren = rawChildren.flatMap {
            if (it is ViewGroup) {
                getAllChildrenControls(it)
            } else {
                controlViews.add(it)
                emptyList<View>()
            }
        }
        controlViews.addAll(grandChildren)
        return controlViews
    }
}