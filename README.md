### 常见的下拉过滤菜单

如下图

![device-2017-08-15-041301](https://github.com/showwiki/ExpandView/blob/master/demo.png)



用法详情见Demo

在根目录的build.gradle 添加maven { url 'https://jitpack.io' }依赖

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```



添加依赖

	dependencies {
	        compile 'com.github.showwiki:ExpandView:1.0.2'
	}


首先在布局文件中定义控件， 自定义属性非必须，但一般你都会要改改颜色，字体,换标题右边的图标什么的，传布局进去就好了，布局规范见下文

```
<com.paulfeng.widget.expandview.ExpandView
    android:id="@+id/ev"
    android:layout_width="match_parent"
    android:layout_height="50dp"
    android:text="Hello World!"
    app:childViewHeight="400dp"
    app:titleTextViewLayout="@layout/tab_title_layout2"
    app:subListviewItemLayout="@layout/expand_drop_list_item2"/>
```



Activity中通过 setTitleAndContent(Map<String, List<String>>)设置数据，为了保证标题的顺序 LinkedHashMap<String, List<String>> 最佳，直接用hashMap会导致标题的排列顺序不对， 如下所示，Map中String为标题数据，List<String>为对应的ListView数据。

```
private HashMap<String,List<String>> initData() {
        LinkedHashMap<String, List<String>> data = new LinkedHashMap<>();
        for(int i = 0; i < 4; i++) {
            ArrayList<String> list = new ArrayList<>();
            for(int j = 0; j < 7; j++) {
                list.add("A" + i + "B" + j);
            }
            data.put(list.get(0), list);
        }

        return data;
    }
```







选中监听，设置 ，让背景变暗也可以在此设置。

```
ev.setOnChooseListener(new ExpandView.OnChooseListener() {
    @Override
    public void onChoose(List<TextView> textLists, int pos) {
        Toast.makeText(MainActivity.this, textLists.get(pos).getText() + "is Choose", Toast.LENGTH_LONG).show();
        tv.setText("Content" + textLists.get(pos).getText());
    }
});
```



如果要更改恢复后其他布局的透明度，可以设置

```
ev.setOnFoldListener(new ExpandView.OnFoldListener() {
    @Override
    public void onFold() {
        
    }
});
```







为了方便更改文字背景颜色，图片等，视图高度等，可以在传入头部TextView的布局app:titleTextViewLayout和下拉Listview的item的布局app:subListviewItemLayout，和限制最大高度 app:childViewHeight但记住需要有符合的id的TextView和ImageView， 如果不设置自定义的属性，则为默认值, 视图高度限制默认在子ListView大于默设置高度时才有效果。



app:titleTextViewLayout的布局示例如下，记得需要有一个id位title的TextView和id为title_pic的ImageView

```
<RelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="0dp"
    android:layout_height="match_parent"
    android:layout_centerInParent="true"
    android:layout_weight="1"
    android:gravity="center">

    <TextView
        android:id="@+id/title"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerVertical="true"
        android:textColor="@android:color/holo_red_dark"
        tools:text="标题1"/>

    <ImageView
        android:id="@+id/title_pic"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerVertical="true"
        android:layout_marginLeft="10dp"
        android:layout_toRightOf="@id/title"
        android:src="@mipmap/arrowdown"/>
</RelativeLayout>
```





app:subListviewItemLayout的布局示例如下，记得需要有一个id位tv的TextView和id为iv的ImageView，其中的图片是选中后状态

```
<RelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/rl_wrapper"
    android:layout_width="match_parent"
    android:background="@android:color/white"
    android:layout_height="45dp">

    <TextView
        android:id="@+id/tv"
        android:layout_width="match_parent"
        android:layout_height="45dp"
        android:layout_centerVertical="true"
        android:layout_marginLeft="14dp"
        android:gravity="center_vertical"
        android:text="test"
        android:textColor="#b33ec227"
        android:textSize="14.0sp"/>

    <ImageView
        android:id="@+id/iv"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentRight="true"
        android:layout_centerVertical="true"
        android:layout_marginRight="20dp"
        android:src="@mipmap/pic_selected"
        android:visibility="invisible"/>
</RelativeLayout>
```

























