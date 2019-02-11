# TouchRecorder

An Android application that facilitates the gathering of touchscreen data. The initial purpose of the application was to gather data and build a dataset to perform machine learning on it.

The user first must insert his data.
The field repetitions indicates how many times each item must be drawn by the same user.
![user_view](docs/images/user_view.png?raw=true)

Then he can start recording, writing or painting on the touchscreen:
![draw_view](docs/images/draw_view.png?raw=true)
![draw_view2](docs/images/draw_view2.png?raw=true)

For each point is recorded the relative time and the connected component in which it appears (if we see the picture as a graph).

The points are highlighted in:

- Green: where an event of TOUCH_DOWN happened
- Red: where an event of TOUCH_UP happened
- White: where an event of TOUCH_MOVE happened

The points are approximated by a Bezier curve, which is displayed in order to give a feedback to the user.
That curve is later sampled, one point each 5 pixel. Note that those points don't have the time component.
![Alt text](docs/images/draw_view3.png?raw=true)

Finally all the data recorded is saved in a .json, together with a screenshot, in a folder of the main storage determined by the user data, the item and the timestamp.

___

It is possible to configure the behaviour of the application through a configuration file, present in the folder created by the application the first time that it is run (if not already present).

```json
{
  "guide_lines": false,
  "items": [
    "item1",
    "item2",
    ...
  ],
  "repetitions": 5,
  "repetitions_label": "Repetitions",
  "title": "Draw: "
}
```
