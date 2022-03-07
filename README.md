# Mustache Core Library for Android

[![](https://jitpack.io/v/mustachedk/core-lib-an.svg)](https://jitpack.io/#mustachedk/core-lib-an)

###### Add this to your root build.gradle
```bash
allprojects {
  repositories {
    maven { url 'https://jitpack.io' }
  }
}
```

###### Add this to your root build.gradle
```bash
dependencies {
  implementation 'com.github.mustachedk:core-lib-an:1.2.13'
}
```
###### Initialize corelib in the custom Application context
```bash
class YourApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //Initialize MustacheCoreLib
        MustacheCoreLib.init(this)
        ...
```

#### Currently Contains:
##### Utilities
* TaskHandler
* NavigationBar
* InternetConnection
* LocationUtil
* NetworkUtil
* PermissionUtil
* PixelUtil
* ScreenUtil
* ValidateUtil
* ViewUtil
* BitmapCropper
* GPSSingleShotCurrentLocation
* Bluetooth enable and isEnabled
* Beacon
* BarcodeBitmapCreator


##### Views
* OneClickButton
* EmptyStateView
* TicketMaterialView
* OrientationAwareRecyclerView

##### Layouts
* PriceLayout
* TicketConstraintLayoutMasked
* AnimatedProgressLayout
* SwipeAcceptLayout
* AnimatedBackgroundLayout
* ToolbarDropdownConstraintLayout

##### Dialogs
* BottomSheetMenu
* BottomSheetPicker
* BottomSheetDoublePicker
* BottomSheetFullscreenDialogFragment
* StandardDialogFragment
* BasicBaseDialogFragment

##### Base Classes:
* DataBindingAdapter (DataBindingViewHolder)
* SectionHeaderListAdapter
* HeaderListViewPagerFragment
* StickyHeaderListAdapter
* SelectableAdapter
