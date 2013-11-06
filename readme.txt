Team Members
=============
David Ouyang Moench (dom289)
Chan-Tang Hsu (hc9757)
Alex Ng Dehe (an8645)

Instructions
============
1. Select material(s) that you want to recycle. Selected material(s) will disappear/remove from the dialog box.
2. Enter address. A list of suggested addresses will appear to minimize typo in address.
	2.1 If address is left empty, the app will use the current location of the device.
3. Click the Go button at the bottom to search for locations.
4. A list of facilities will show up sort according to the distance from the entered address.
	4.1 A list of other accepted materials by the facilities is displayed.
	4.2 Address of the facility is showed as well.
5. Select one of the facilities will bring you to the facility's details page.
	5.1 A map showing the location of the facility is shown.
	5.2 Press the call button to contact the facility.
	5.3 Press the get direction button to get turn by turn navigation to the facility.

List of Completed Features
===========================
1. Location autocomplete by Google.
2. Display list of facilities sorted by distance from specified address.
3. Show all accepted recycling materials by the facilities.
4. Show the location of the facility on a map.
5. Button that can call the facility.
6. Button that opens up Google Maps for turn by turn navigation.

List of Incomplete Features
===========================
1. Austin recycling pickup schedule lookup capability.
2. Point/Reward system.
3. Distance from specified address.

List of Added Features
======================
1. Use GPS data of the device to find current location.
2. Check internet connection in splash screen.
3. Check GPS status when no address is entered. 

List of Classes and Chunks of Code from Other Sources
======================================================
1.	Classes: LocationAutoCompleteAdapter & MainActivity.PlacesTask
	Source: ("Adding Autocomplete to your Android App", https://developers.google.com/places/training/autocomplete-android)

2. 	Classes: MaterialListAdapter.ViewHolder & ResultListAdapter.ViewHolder
	Source: ("Android ListView - Tutorial", http://www.vogella.com/articles/AndroidListView/article.html#androidlists_overview)

3.	Class: Parcelable in FacilityItem
	Source: ("How to properly implement Parcelable with an ArrayList<Parcelable>?", http://stackoverflow.com/questions/7042272/how-to-properly-implement-parcelable-with-an-arraylistparcelable)


List of Classes and Chunks of Code Completed Ourselves
======================================================
1. MainActivity
2. ResultListActivity
3. FacilityDetailsActivity
4. SplashScreenActivity
5. MaterialItem
6. MaterialListAdapter
7. ResultListAdapter
