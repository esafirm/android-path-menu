package com.path.menu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import com.path.menu.common.SimpleAnimationListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Provides a "Path" like menu for android.
 *
 * @author Siyamed SINIR, Esafirm
 */
public class PathMenu extends FrameLayout {

  private static final int DEFAULT_SATELLITE_DISTANCE = 200;
  private static final float DEFAULT_TOTAL_SPACING_DEGREES = 90f;
  private static final boolean DEFAULT_CLOSE_ON_CLICK = true;
  private static final int DEFAULT_EXPAND_DURATION = 400;

  private Animation mainRotateRight;
  private Animation mainRotateLeft;

  private ImageView imgMain;
  private LayoutInflater inflater;
  private OnMenuClickListener itemClickedListener;
  private InternalSatelliteOnClickListener internalItemClickListener;

  private List<PathMenuItem> menuItems = new ArrayList<>();
  private Map<View, PathMenuItem> viewToItemMap = new HashMap<>();

  private AtomicBoolean plusAnimationActive = new AtomicBoolean(false);

  // ?? how to save/restore?
  private DegreeProvider gapDegreesProvider = new DefaultDegreeProvider();

  //States of these variables are saved
  private boolean rotated = false;
  private int measureDiff = 0;

  //States of these variables are saved - Also configured from XML
  private float totalSpacingDegree = DEFAULT_TOTAL_SPACING_DEGREES;
  private int satelliteDistance = DEFAULT_SATELLITE_DISTANCE;
  private int expandDuration = DEFAULT_EXPAND_DURATION;
  private boolean closeItemsOnClick = DEFAULT_CLOSE_ON_CLICK;

  /* --------------------------------------------------- */
  /* > Constructors */
  /* --------------------------------------------------- */

  public PathMenu(Context context) {
    this(context, null);
  }

  public PathMenu(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PathMenu(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context, attrs, defStyle);
  }

  /* --------------------------------------------------- */
  /* > Internal */
  /* --------------------------------------------------- */

  private void init(Context context, AttributeSet attrs, int defStyle) {
    inflater = LayoutInflater.from(context);
    inflater.inflate(R.layout.sat_main, this, true);
    imgMain = (ImageView) findViewById(R.id.sat_main);

    if (attrs != null) {
      TypedArray typedArray =
          context.obtainStyledAttributes(attrs, R.styleable.PathMenu, defStyle, 0);
      satelliteDistance = typedArray.getDimensionPixelSize(R.styleable.PathMenu_satelliteDistance,
          DEFAULT_SATELLITE_DISTANCE);
      totalSpacingDegree = typedArray.getFloat(R.styleable.PathMenu_totalSpacingDegree,
          DEFAULT_TOTAL_SPACING_DEGREES);
      closeItemsOnClick =
          typedArray.getBoolean(R.styleable.PathMenu_closeOnClick, DEFAULT_CLOSE_ON_CLICK);
      expandDuration =
          typedArray.getInt(R.styleable.PathMenu_expandDuration, DEFAULT_EXPAND_DURATION);
      typedArray.recycle();
    }

    mainRotateLeft = AnimatorCreator.createMainButtonAnimation(context);
    mainRotateRight = AnimatorCreator.createMainButtonInverseAnimation(context);

    Animation.AnimationListener plusAnimationListener = new SimpleAnimationListener() {
      @Override public void onAnimationEnd(Animation animation) {
        plusAnimationActive.set(false);
      }
    };

    mainRotateLeft.setAnimationListener(plusAnimationListener);
    mainRotateRight.setAnimationListener(plusAnimationListener);

    imgMain.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onMainButtonClick();
      }
    });
    internalItemClickListener = new InternalSatelliteOnClickListener(this);
  }

  private void onMainButtonClick() {
    if (plusAnimationActive.compareAndSet(false, true)) {
      if (!rotated) {
        imgMain.startAnimation(mainRotateLeft);
        for (PathMenuItem item : menuItems) {
          item.getView().startAnimation(item.getOutAnimation());
        }
      } else {
        imgMain.startAnimation(mainRotateRight);
        for (PathMenuItem item : menuItems) {
          item.getView().startAnimation(item.getInAnimation());
        }
      }
      rotated = !rotated;
    }
  }

  private void openItems() {
    if (plusAnimationActive.compareAndSet(false, true)) {
      if (!rotated) {
        imgMain.startAnimation(mainRotateLeft);
        for (PathMenuItem item : menuItems) {
          item.getView().startAnimation(item.getOutAnimation());
        }
      }
      rotated = !rotated;
    }
  }

  private void closeItems() {
    if (plusAnimationActive.compareAndSet(false, true)) {
      if (rotated) {
        imgMain.startAnimation(mainRotateRight);
        for (PathMenuItem item : menuItems) {
          item.getView().startAnimation(item.getInAnimation());
        }
      }
      rotated = !rotated;
    }
  }

  public void addItems(List<PathMenuItem> items) {

    menuItems.addAll(items);
    removeView(imgMain);

    float[] degrees = getDegrees(menuItems.size());
    int index = 0;
    for (PathMenuItem menuItem : menuItems) {
      int finalX = AnimatorCreator.getTranslateX(degrees[index], satelliteDistance);
      int finalY = AnimatorCreator.getTranslateY(degrees[index], satelliteDistance);

      ImageView itemView = (ImageView) inflater.inflate(R.layout.sat_item_cr, this, false);
      itemView.setTag(menuItem.getId());
      itemView.setVisibility(View.INVISIBLE);
      //itemView.setOnClickListener(internalItemClickListener);
      itemView.setOnClickListener(new OnClickListener() {
        @Override public void onClick(View view) {
          Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT).show();
        }
      });

      Animation itemOut =
          AnimatorCreator.createItemOutAnimation(index, expandDuration, finalX, finalY);
      Animation itemIn =
          AnimatorCreator.createItemInAnimation(index, expandDuration, finalX, finalY);
      Animation itemClick = AnimatorCreator.createItemClickAnimation(getContext());

      menuItem.setView(itemView);
      menuItem.setInAnimation(itemIn);
      menuItem.setOutAnimation(itemOut);
      menuItem.setClickAnimation(itemClick);

      itemIn.setAnimationListener(new PathAnimationListener(itemView, true, viewToItemMap));
      itemOut.setAnimationListener(new PathAnimationListener(itemView, false, viewToItemMap));
      itemClick.setAnimationListener(new PathItemClickListener(this, menuItem.getId()));

      if (menuItem.getImgResourceId() > 0) {
        itemView.setImageResource(menuItem.getImgResourceId());
      } else if (menuItem.getImgDrawable() != null) {
        itemView.setImageDrawable(menuItem.getImgDrawable());
      }

      addView(itemView);

      viewToItemMap.put(itemView, menuItem);
      index++;
    }

    addView(imgMain);
  }

  private float[] getDegrees(int count) {
    return gapDegreesProvider.getDegrees(count, totalSpacingDegree);
  }

  private void recalculateMeasureDiff() {
    int itemWidth = 0;
    if (menuItems.size() > 0) {
      itemWidth = menuItems.get(0).getView().getWidth();
    }
    measureDiff = Float.valueOf(satelliteDistance * 0.2f).intValue() + itemWidth;
  }

  private void resetItems() {
    if (menuItems.size() > 0) {
      List<PathMenuItem> items = new ArrayList<>(menuItems);
      menuItems.clear();
      this.removeAllViews();
      addItems(items);
    }
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    recalculateMeasureDiff();

    int totalHeight = imgMain.getHeight() + satelliteDistance + measureDiff;
    int totalWidth = imgMain.getWidth() + satelliteDistance + measureDiff;
    setMeasuredDimension(totalWidth, totalHeight);
  }

  public Map<View, PathMenuItem> getViewToItemMap() {
    return viewToItemMap;
  }

  /* --------------------------------------------------- */
  /* > Public Methods */
  /* --------------------------------------------------- */

  /**
   * Sets the click listener for satellite items.
   */
  public void setOnItemClickedListener(OnMenuClickListener itemClickedListener) {
    this.itemClickedListener = itemClickedListener;
  }

  /**
   * Defines the algorithm to define the gap between each item.
   * Note: Calling before adding items is strongly recommended.
   */
  public void setGapDegreeProvider(DegreeProvider gapDegreeProvider) {
    this.gapDegreesProvider = gapDegreeProvider;
    resetItems();
  }

  /**
   * Defines the total space between the initial and final item in degrees.
   * Note: Calling before adding items is strongly recommended.
   *
   * @param totalSpacingDegree The degree between initial and final items.
   */
  public void setTotalSpacingDegree(float totalSpacingDegree) {
    this.totalSpacingDegree = totalSpacingDegree;
    resetItems();
  }

  /**
   * Sets the distance of items from the center in pixels.
   * Note: Calling before adding items is strongly recommended.
   *
   * @param distance the distance of items to center in pixels.
   */
  public void setSatelliteDistance(int distance) {
    this.satelliteDistance = distance;
    resetItems();
  }

  /**
   * Sets the duration for expanding and collapsing the items in miliseconds.
   * Note: Calling before adding items is strongly recommended.
   *
   * @param expandDuration the duration for expanding and collapsing the items in miliseconds.
   */
  public void setExpandDuration(int expandDuration) {
    this.expandDuration = expandDuration;
    resetItems();
  }

  /**
   * Sets the image resource for the center button.
   *
   * @param resource The image resource.
   */
  public void setMainImage(int resource) {
    this.imgMain.setImageResource(resource);
  }

  /**
   * Sets the image drawable for the center button.
   *
   * @param drawable The image drawable.
   */
  public void setMainImage(Drawable drawable) {
    this.imgMain.setImageDrawable(drawable);
  }

  /**
   * Defines if the menu shall collapse the items when an item is clicked. Default value is true.
   */
  public void setCloseItemsOnClick(boolean closeItemsOnClick) {
    this.closeItemsOnClick = closeItemsOnClick;
  }

  /**
   * Expand the menu items.
   */
  public void expand() {
    openItems();
  }

  /**
   * Collapse the menu items
   */
  public void close() {
    closeItems();
  }

  /* --------------------------------------------------- */
  /* > OnClick */
  /* --------------------------------------------------- */

  /**
   * The listener class for item click event.
   *
   * @author Siyamed SINIR
   */
  public interface OnMenuClickListener {
    /**
     * When an item is clicked, informs with the id of the item, which is given while adding the
     * items.
     *
     * @param id The id of the item.
     */
    void onClick(int id);
  }

  private static class PathItemClickListener extends SimpleAnimationListener {
    private WeakReference<PathMenu> menuRef;
    private int tag;

    public PathItemClickListener(PathMenu menu, int tag) {
      this.menuRef = new WeakReference<>(menu);
      this.tag = tag;
    }

    @Override public void onAnimationStart(Animation animation) {
      PathMenu menu = menuRef.get();
      if (menu != null && menu.closeItemsOnClick) {
        menu.close();
        if (menu.itemClickedListener != null) {
          menu.itemClickedListener.onClick(tag);
        }
      }
    }
  }

  private static class InternalSatelliteOnClickListener implements View.OnClickListener {
    private WeakReference<PathMenu> menuRef;

    public InternalSatelliteOnClickListener(PathMenu menu) {
      this.menuRef = new WeakReference<>(menu);
    }

    @Override public void onClick(View v) {
      PathMenu menu = menuRef.get();
      if (menu != null && v.getVisibility() == View.VISIBLE) {
        PathMenuItem menuItem = menu.getViewToItemMap().get(v);
        v.startAnimation(menuItem.getClickAnimation());
      }
    }
  }

  /* --------------------------------------------------- */
  /* > Utils */
  /* --------------------------------------------------- */

  private static class PathAnimationListener implements Animation.AnimationListener {

    private WeakReference<View> viewRef;
    private boolean isInAnimation;
    private Map<View, PathMenuItem> viewToItemMap;

    public PathAnimationListener(View view, boolean isIn, Map<View, PathMenuItem> viewToItemMap) {
      this.viewRef = new WeakReference<>(view);
      this.isInAnimation = isIn;
      this.viewToItemMap = viewToItemMap;
    }

    @Override public void onAnimationStart(Animation animation) {
      if (viewRef == null) return;
      View view = viewRef.get();
      if (view != null) {
        viewToItemMap.get(view).getView().setVisibility(View.VISIBLE);
      }
    }

    @Override public void onAnimationRepeat(Animation animation) {
    }

    @Override public void onAnimationEnd(Animation animation) {
      if (viewRef == null) return;
      View view = viewRef.get();
      if (view != null) {
        viewToItemMap.get(view)
            .getView()
            .setVisibility(isInAnimation
                ? View.INVISIBLE
                : View.VISIBLE);
      }
    }
  }

  /* --------------------------------------------------- */
  /* > Parcelable */
  /* --------------------------------------------------- */

  @Override protected Parcelable onSaveInstanceState() {
    Parcelable superState = super.onSaveInstanceState();
    SavedState ss = new SavedState(superState);
    ss.rotated = rotated;
    ss.totalSpacingDegree = totalSpacingDegree;
    ss.satelliteDistance = satelliteDistance;
    ss.measureDiff = measureDiff;
    ss.expandDuration = expandDuration;
    ss.closeItemsOnClick = closeItemsOnClick;
    return ss;
  }

  @Override protected void onRestoreInstanceState(Parcelable state) {
    SavedState ss = (SavedState) state;
    rotated = ss.rotated;
    totalSpacingDegree = ss.totalSpacingDegree;
    satelliteDistance = ss.satelliteDistance;
    measureDiff = ss.measureDiff;
    expandDuration = ss.expandDuration;
    closeItemsOnClick = ss.closeItemsOnClick;

    super.onRestoreInstanceState(ss.getSuperState());
  }

  static class SavedState extends BaseSavedState {
    boolean rotated;
    private float totalSpacingDegree;
    private int satelliteDistance;
    private int measureDiff;
    private int expandDuration;
    private boolean closeItemsOnClick;

    SavedState(Parcelable superState) {
      super(superState);
    }

    public SavedState(Parcel in) {
      super(in);
      rotated = Boolean.valueOf(in.readString());
      totalSpacingDegree = in.readFloat();
      satelliteDistance = in.readInt();
      measureDiff = in.readInt();
      expandDuration = in.readInt();
      closeItemsOnClick = Boolean.valueOf(in.readString());
    }

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel out, int flags) {
      out.writeString(Boolean.toString(rotated));
      out.writeFloat(totalSpacingDegree);
      out.writeInt(satelliteDistance);
      out.writeInt(measureDiff);
      out.writeInt(expandDuration);
      out.writeString(Boolean.toString(closeItemsOnClick));
    }

    public static final Parcelable.Creator<SavedState> CREATOR =
        new Parcelable.Creator<SavedState>() {
          public SavedState createFromParcel(Parcel in) {
            return new SavedState(in);
          }

          public SavedState[] newArray(int size) {
            return new SavedState[size];
          }
        };
  }
}
