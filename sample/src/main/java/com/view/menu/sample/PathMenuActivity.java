package com.view.menu.sample;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.menu.R;
import com.path.menu.CounterDegreeProvider;
import com.path.menu.PathMenu;
import com.path.menu.PathMenu.OnMenuClickListener;
import com.path.menu.PathMenuItem;
import java.util.ArrayList;
import java.util.List;

public class PathMenuActivity extends Activity {

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    PathMenu menu = (PathMenu) findViewById(R.id.menu);
    menu.setSatelliteDistance(dp2Px(100));
    menu.setTotalSpacingDegree(90f);
    menu.setGapDegreeProvider(new CounterDegreeProvider());

    List<PathMenuItem> items = new ArrayList<>();
    items.add(new PathMenuItem(4, R.drawable.ic_1));
    items.add(new PathMenuItem(4, R.drawable.ic_3));
    items.add(new PathMenuItem(4, R.drawable.ic_4));
    items.add(new PathMenuItem(3, R.drawable.ic_5));
    menu.addItems(items);

    menu.setOnItemClickedListener(new OnMenuClickListener() {
      public void onClick(int id) {
        Log.i("sat", "Clicked on " + id);
      }
    });
  }

  private int dp2Px(int dp) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
        Resources.getSystem().getDisplayMetrics());
  }
}