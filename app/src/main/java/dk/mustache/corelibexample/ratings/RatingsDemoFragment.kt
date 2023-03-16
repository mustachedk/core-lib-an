package dk.mustache.corelibexample.ratings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dk.mustache.corelib.rating_dialog.*
import dk.mustache.corelib.views.ErrorDialog
import dk.mustache.corelibexample.databinding.FragmentRatingsDemoBinding

class RatingsDemoFragment : Fragment() {

    lateinit var binding: FragmentRatingsDemoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRatingsDemoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDemoFragment()

        /**
         * Quick Deployment - Increase number of app opens by 1 with
         * "incrementNumberAppOpens". This usually occurs in App/MainActivity. Then
         * use "showRatingDialogIfLaunchCountSufficient" in the relevant fragment,
         * which will show the rating dialog if the minLaunchCount has been
         * reached. ShowNeverButton is set to true, so the user can say they never
         * want to see the dialog again.
         */
        binding.btnSimulateAppOpening.setOnClickListener {
            AppRatingManager.incrementNumberAppOpens()
            showNumberAppOpenings()

            AppRatingManager.showRatingDialogIfLaunchCountSufficient(requireActivity(), true) {
                // Do tracking or other actions that should only happen if the app was opened
            }
        }

        /**
         * More customizable deployment - Increase number of app opens by 1 with
         * "incrementNumberAppOpens". This usually occurs in App/MainActivity. Then
         * call "shouldRatingDialogOpen" in the relevant fragment to check if the
         * dialog should be opened. Then open the dialog with "showRatingDialog".
         */
        val expandedOpening = {
            AppRatingManager.incrementNumberAppOpens()
            if (AppRatingManager.shouldRatingDialogOpen()) {
                // Do tracking or other actions that should only happen if the app will open
                AppRatingManager.showRatingDialog(requireActivity(), true)
            }
        }

        /**
         * Open rating dialog manually (from settings for instance). Usually we do
         * not show ShowNeverButton in the dialog in these cases.
         */
        binding.btnOpenRatingDialog.setOnClickListener {
            AppRatingManager.showRatingDialog(requireActivity())
        }

        binding.btnResetNumAppOpenings.setOnClickListener {
            AppRatingManager.resetNumberAppOpenings()
            showNumberAppOpenings()
        }

        binding.btnResetShownThisSession.setOnClickListener {
            AppRatingManager.resetShownThisSession()
            showNumberAppOpenings()
        }

        binding.btnResetNeverShow.setOnClickListener {
            AppRatingManager.resetNeverShowRatingDialogAgain()
            showNumberAppOpenings()
        }

        /**
         * Set config to default, with OpenStore action and OpenEmail action.
         * Setting config should usually occur in App/MainActivity. If you
         * have language localization, it should occur after language is set.
         */
        binding.btnSetDefaultStoreEmailDialog.setOnClickListener {
            val config = AppRatingConfigBuilder(requireContext())
                .setPositiveRating(OpenStoreRatingAction("dk.ku.myucph"))
                .setNegativeRating(OpenEmailRatingAction(emailRecipient = "mustache@mustache.dk"))
                .setOnDialogueCompleted { showNumberAppOpenings() }
                .build()

            AppRatingManager.setAppRatingConfig(config)
        }

        /**
         * Set to a custom config to default, with OpenStore action and a
         * custom negative action. Setting config should usually occur in
         * App/MainActivity. If you have language localization, it should occur
         * after language is set.
         */
        binding.btnSetCustomDialog.setOnClickListener {
            val config = AppRatingConfigBuilder(requireContext())
                .setMainDialogStrings(
                    title = "Hvad synes du om demo app'en?",
                    message = "Giv os demo stjerner fra 1–5, hvor 5 er bedst."
                    // Vi lader Delay og Never knapperne bruge default værdier
                )
                .setMinLaunchCount(5)
                .setMinStarsForPositive(3)
                .setPositiveRating(OpenStoreRatingAction("dk.ku.myucph")) // Use a default action
                .setNegativeRating {
                    // Set a custom action
                    ErrorDialog(
                        title = "Low Rating", message = "This rating is low enough that we want " +
                                "feedback rather than send the user to store. In a normal app we would probably " +
                                "just open an email client, for the user to send feedback in."
                    ).show(this)
                }
                .setDisableOnNegativeAction(false)
                .setOnDialogueCompleted { showNumberAppOpenings() }
                .build()

            AppRatingManager.setAppRatingConfig(config)
        }
    }

    //
    // Demo Fragment stuff
    //

    private fun setupDemoFragment() {
        binding.txtAppOpenings.setOnClickListener {
            showNumberAppOpenings()
        }

        val config = AppRatingConfigBuilder(requireContext())
            .setPositiveRating(OpenStoreRatingAction("dk.ku.myucph"))
            .setNegativeRating(OpenEmailRatingAction(emailRecipient = "mustache@mustache.dk"))
            .setOnDialogueCompleted { showNumberAppOpenings() }
            .build()

        AppRatingManager.setAppRatingConfig(config)
        showNumberAppOpenings()
    }

    private fun showNumberAppOpenings() {
        var text = "App has opened " + AppRatingSharedPrefs.numberAppOpenings + " times."
        if (AppRatingSharedPrefs.ratingDialogOptedOut) {
            text += "\n- User has opted out of the rating dialog"
            binding.btnResetNeverShow.visibility = View.VISIBLE
        } else {
            binding.btnResetNeverShow.visibility = View.GONE
        }
        if (AppRatingManager.wasShownThisSession()) {
            text += "\n- The 'shown this session' flag is set, so the dialog won't show again."
            binding.btnResetShownThisSession.visibility = View.VISIBLE
        } else {
            binding.btnResetShownThisSession.visibility = View.GONE
        }
        binding.txtAppOpenings.text = text
    }
}