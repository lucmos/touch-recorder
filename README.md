# Biotouch

An Android application that facilitates the gathering of touchscreen data. The initial purpose of the application was to gather data and build a dataset to perform machine learning on it.

The user first must insert his data. The words field determines how many times the user must write something.
![](docs/images/user_view.png?raw=true)


Then he can start recording, writing or painting on the touchscreen:
![Alt text](docs/images/draw_view.png?raw=true)
![Alt text](docs/images/draw_view2.png?raw=true)

For each point is recorded the relative time and the connected component in which it appears (if we see the picture as a graph).

The points are:

- Green: where an event of TOUCH_DOWN happened
- Red: where an event of TOUCH_UP happened
- White: where an event of TOUCH_MOVE happened

The points are approximated by a Bezier curve. That curve is later sampled, one point each 5 pixel. (those points don't have the time component)
![Alt text](docs/images/draw_view3.png?raw=true)

Finally all the data recorded is saved in a .json, togheter with a screenshot, in a folder of the main storage determined by the user data.
