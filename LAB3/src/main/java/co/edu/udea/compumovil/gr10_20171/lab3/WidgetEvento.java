package co.edu.udea.compumovil.gr10_20171.lab3;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import java.util.ArrayList;

import datos.EventDataBaseAdapter;
import datos.Evento;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetEvento extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // ller eventos de base de datos local
        EventDataBaseAdapter gestor_eventos = new EventDataBaseAdapter(context);
        gestor_eventos = gestor_eventos.open();

        ArrayList<Evento> eventos = new ArrayList<Evento>();
        eventos = gestor_eventos.lista_eventos();

        gestor_eventos.close();

        Evento ultimo = null;Integer id = 0;

        for(Evento e: eventos){

            // ontener último evento (mayor ID)
            if(e.getID() > id){
                ultimo = e;
            }

        }

        CharSequence widgetText = "Último evento: " + ultimo.getNombre();
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_evento);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

