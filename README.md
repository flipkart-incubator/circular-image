# CircularImage

**Description**
CircularImage is an android library to create a configurable CircularDrawables. It has the following functionalities:

1. Combines upto 4 bitmaps to create one combined CircularDrawable. Additionally, it supports combination of 2 and 3 bitmaps.
2. Supports drawing of text, if bitmaps are not available.
3. Supports drawing of different types of configurable notifications, at any random angle x from horizontal.
4. Supports badges at a fixed position.
5. Supports configurable dividers and borders.
6. Optimised to reduce overdraw.

**Screenshot:**
![Alt text](/Screenshots/Screenshot_2015-05-14-18-24-34.png?raw=true "Screenshot")

**Sample Code**
'<addr>'
CircularDrawable circularDrawable = new CircularDrawable();
circularDrawable.setBitmapOrText(bitmap1, bitmap2);
//Set Notification
circularDrawable.setNotificationDrawer(new CircularNotificationDrawer().setNotificationText("51").setNotificationAngle(135).setNotificationColor(Color.WHITE, Color.RED));
//Set Divider
circularDrawable.setDivider(4, Color.WHITE);
circularDrawable.setBadge(badgeIcon);
circularDrawable.setBorder(Color.BLACK, 4);
//Set the drawable to imageView
imageView.setImageDrawable(circularDrawable)
'</addr>'
