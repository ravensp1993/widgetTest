package time2learnarticle.www.rug.nl.time2learnarticle;

import java.util.ArrayList;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

/**
 * If you are familiar with Adapter of ListView,this is the same as adapter
 * with few changes
 */
public class ListProvider implements RemoteViewsFactory {
    private ArrayList<ListItem> listItemList = new ArrayList<ListItem>();
    private Context context = null;
    private int appWidgetId;

    public ListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        populateListItem();
    }

    private void populateListItem() {
        ListItem item0 = new ListItem("Article 1", "FAKE TEXT ASDAD", "https://www.google.com");
        listItemList.add(item0);
        item0 = new ListItem("Article 2", "FAKE TEXT ASDAD", "https://www.amazon.com");
        listItemList.add(item0);
        item0 = new ListItem("Article 3", "FAKE TEXT ASDAD", "https://www.ebay.com");
        listItemList.add(item0);
        item0 = new ListItem("Article 4", "FAKE TEXT ASDAD", "https://www.yahoo.com");
        listItemList.add(item0);
        item0 = new ListItem("Article 5", "FAKE TEXT ASDAD", "https://www.google.com");
        listItemList.add(item0);
        item0 = new ListItem("Article 6", "FAKE TEXT ASDAD", "https://www.google.com");
        listItemList.add(item0);
        item0 = new ListItem("Article 7", "FAKE TEXT ASDAD", "https://www.google.com");
        listItemList.add(item0);
        item0 = new ListItem("Article 8", "FAKE TEXT ASDAD", "https://www.google.com");
        listItemList.add(item0);
        item0 = new ListItem("Article 9", "FAKE TEXT ASDAD", "https://www.google.com");
        listItemList.add(item0);
        item0 = new ListItem("Article 10", "FAKE TEXT ASDAD", "https://www.google.com");
        listItemList.add(item0);
    }


    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     *Similar to getView of Adapter where instead of View
     *we return RemoteViews
     *
     */
    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.article_list_item);

        ListItem listItem = listItemList.get(position);
        remoteView.setTextViewText(R.id.article_title, listItem.heading);
        remoteView.setTextViewText(R.id.article_text, listItem.content);


        Bundle extras = new Bundle();
        extras.putInt(WidgetProvider.GET_EXTRA, position);
        extras.putString(WidgetProvider.GET_EXTRA, listItemList.get(position).url);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        // Make it possible to distinguish the individual on-click
        // action of a given item
        remoteView.setOnClickFillInIntent(R.id.widget_item, fillInIntent);

        return remoteView;
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
    }

}
