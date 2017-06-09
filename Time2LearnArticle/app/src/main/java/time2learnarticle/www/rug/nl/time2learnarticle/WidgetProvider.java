package time2learnarticle.www.rug.nl.time2learnarticle;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.net.URI;

public class WidgetProvider extends AppWidgetProvider {
	public static final String GET_EXTRA = "www.rug.nl.time2learnarticle.GET_EXTRA";
	public static final String TOAST_ACTION = "www.rug.nl.time2learnarticle.TOAST_ACTION";
	public String articleURL;
	/* 
	 * this method is called every 30 mins as specified on widgetinfo.xml
	 * this method is also called on every phone reboot
	 */

	@Override
	public void onReceive(Context context, Intent intent) {
		AppWidgetManager mgr = AppWidgetManager.getInstance(context);
		if (intent.getAction().equals(TOAST_ACTION)) {
			int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			int viewIndex = intent.getIntExtra(GET_EXTRA, 0);
			articleURL = intent.getStringExtra(GET_EXTRA);

			Intent intentURL = new Intent(Intent.ACTION_VIEW,
					Uri.parse(articleURL));
			intentURL.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intentURL);

			Toast.makeText(context, "Touched view " + viewIndex + articleURL, Toast.LENGTH_SHORT).show();
		}
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		final int N = appWidgetIds.length;
		/*int[] appWidgetIds holds ids of multiple instance of your widget
		 * meaning you are placing more than one widgets on your homescreen*/
		for (int i = 0; i < N; ++i) {

			// Sets up the intent that points to the StackViewService that will
			// provide the views for this collection.
			Intent intent = new Intent(context, WidgetService.class);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
			// When intents are compared, the extras are ignored, so we need to embed the extras
			// into the data so that the extras will not be ignored.
			intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
			RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			rv.setRemoteAdapter(appWidgetIds[i], R.id.listViewWidget, intent);

			// The empty view is displayed when the collection has no items. It should be a sibling
			// of the collection view.
			rv.setEmptyView(R.id.listViewWidget, R.id.empty_view);

			// This section makes it possible for items to have individualized behavior.
			// It does this by setting up a pending intent template. Individuals items of a collection
			// cannot set up their own pending intents. Instead, the collection as a whole sets
			// up a pending intent template, and the individual items set a fillInIntent
			// to create unique behavior on an item-by-item basis.
			Intent toastIntent = new Intent(context, WidgetProvider.class);
			// Set the action for the intent.
			// When the user touches a particular view, it will have the effect of
			// broadcasting TOAST_ACTION.
			toastIntent.setAction(WidgetProvider.TOAST_ACTION);
			toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
			intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
			PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			rv.setPendingIntentTemplate(R.id.listViewWidget, toastPendingIntent);


			appWidgetManager.updateAppWidget(appWidgetIds[i], rv);

		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	private RemoteViews updateWidgetListView(Context context, int appWidgetId) {

		//which layout to show on widget
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);
		
		//RemoteViews Service needed to provide adapter for ListView
		Intent svcIntent = new Intent(context, WidgetService.class);
		//passing app widget id to that RemoteViews Service
		svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		//setting a unique Uri to the intent
		//don't know its purpose to me right now
		svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
		//setting adapter to listview of the widget
		remoteViews.setRemoteAdapter(appWidgetId, R.id.listViewWidget,
				svcIntent);
		//setting an empty view in case of no data
		remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);
		return remoteViews;
	}

}
