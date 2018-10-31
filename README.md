# Android-UIKit-Foundation

## Info

A fragment controller foundation for Android, which tryes to mimic iOS UIKit behavior

## What is done

- iOS-like navigation bar, with slide in/out content views
- Swipe from the left corner gesture to close the current view
- Easy to use Tab bar

### UIViewControllerContainer 

A container which extends ConstraintLayout, must be the root of layout for UIViewController

### UIViewController 

Abstract class which extends Fragment, implements the functionality to be used as child for UINavigationController, contains functions to use:
- createView(LayoutInflater inflater, ViewGroup container, @LayoutRes int resource) - return UIViewControllerContainer, must be used as super for onCreateView()
- setTitle(String title) - set the title which will appears on UINavigationController
- getTitle() - return current controllers title
- present(UIViewController controller) - add a new controller to the controllers stack and show it on the screen using animation
- dismiss() - remove current controller from the screen and from controllers stack in UINavigationController

### UINavigationController

Class which extends Fragment, implements the functionality to store and animate UIViewControllers attached to it, contains functions to use:
- init(UIViewController rootController) - return UIViewController with root controller in it, must be used as initialiser 
- present(UIViewController controller) - add a new controller to the controllers stack and show it on the screen using animation
- dismiss() - remove current controller from the screen and from controllers stack

### UITabBarController

Class which extends Fragment, implements the functionality to store fragments and show them on the screen using BottomNavigationView, contains functions to use:
- init(@MenuRes int menu, Fragment[] rootFragments) - return UITabBarController, menu resource and root fragments array must have an equal sizes, must be used as initialiser 

## What to do

- Add more iOS-like animations
- Support for menus in navigation controller
- ??? ... may be something more
