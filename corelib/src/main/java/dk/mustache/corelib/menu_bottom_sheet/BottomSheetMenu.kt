package dk.mustache.mapdiet.fragments.bottomsheets

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import dk.mustache.corelib.R
import dk.mustache.corelib.databinding.FragmentBottomsheetMenuBinding
import dk.mustache.corelib.databinding.ItemMenuOptionBinding
import dk.mustache.corelib.databinding.ItemMenuOptionDividerBinding
import dk.mustache.corelib.menu_bottom_sheet.BottomSheetDialogSettings
import dk.mustache.corelib.menu_bottom_sheet.MenuDialogType
import java.lang.Appendable
import java.util.ArrayList

class BottomSheetMenu : BottomSheetDialogFragment() {

    private var mListener: BottomSheetMenuListener? = null
    private lateinit var settings: BottomSheetDialogSettings
    private lateinit var binding: FragmentBottomsheetMenuBinding
    private val handler = Handler(Looper.getMainLooper())

    interface BottomSheetMenuListener {
        fun itemSelected(
                text: String,
                index: Int,
                menuType: MenuDialogType
        )
        fun nothingSelected()
    }

    companion object {
        private const val DIALOG_SETTINGS = "dialog_settings"

        fun newInstance(dialogSettings: BottomSheetDialogSettings): BottomSheetMenu {
            val fragment = BottomSheetMenu()

            val args = Bundle()
            args.putParcelable(DIALOG_SETTINGS, dialogSettings)

            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBottomsheetMenuBinding.inflate(inflater, container, false)

        if (arguments!=null) {
            settings = arguments?.getParcelable(DIALOG_SETTINGS)?:BottomSheetDialogSettings("Menu Header", listOf("Menu Option 1", "Menu Option 2"), MenuDialogType.CUSTOM)

        }

        binding.menuHeader.text = settings.title

        settings.itemTextList.forEachIndexed { index, itemText ->
            val optionItem = ItemMenuOptionBinding.inflate(inflater)
            optionItem.menuOption1.text = itemText
            optionItem.root.setOnClickListener {
                optionItem.menuItemBackground.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transBlack))
                handler.postDelayed({
                    if (isAdded) {
                        optionItem.menuItemBackground.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                        dismiss()
                    }
                    mListener?.itemSelected(itemText, index, settings.type)
                },200)
            }
            binding.menuOptions.addView(optionItem.root)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.BottomSheetDialogStyle
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val cardDetailsListener = parentFragment
        if (cardDetailsListener is BottomSheetMenuListener)
            mListener = cardDetailsListener
        else if (activity is BottomSheetMenuListener){
            mListener = activity as BottomSheetMenuListener
        } else
            throw RuntimeException(parentFragment?.tag + " must implement BottomSheetMenuListener")
    }

    override fun onDetach() {
        super.onDetach()
        mListener?.nothingSelected()
        mListener = null
    }
}