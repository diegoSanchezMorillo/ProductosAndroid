package com.productos.app;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Solari on 23/05/14.
 */
public class ProductListFragment extends ListFragment {

    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private Callbacks mCallbacks = sDummyCallbacks;
    private int mActivatedPosition = ListView.INVALID_POSITION;

    //Direcciones desde las que se obtendrán los datos
    public static final String URL_DATA = "http://www.xml.esy.es/productos.xml";
    public static final String URL_IMAGES = "http://pruebas.javiergarbedo.es/uploadFiles/";

    //Almacenará el contexto (Activity) en el que se encuentra este fragment, ya que será necesario para abrir la BD
    private Context context;

    public interface Callbacks {
        public void onItemSelected(String id);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    public ProductListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Este bloque, con algunos cambios, se ha trasladado al método showPersonList
//        setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(
//                getActivity(),
//                android.R.layout.simple_list_item_activated_1,
//                android.R.id.text1,
//                DummyContent.ITEMS));

        //Descargar los datos del documento XML
        ProductAppDownloader productAppDownloader = new ProductAppDownloader(context, this);
        productAppDownloader.execute(URL_DATA);
        // Mostrar la lista que haya de momento en la BD, hasta que finalice la descarga anterior
        showProductList();
    }

    //Este método también es ejecutado desde el onPostExecute de la descarga del XML
    public void showProductList() {
        //Conectar con la BD y obtener los datos necesarios para rellenar la lista
        ProductDBManagerAndroid productDBManagerAndroid = new ProductDBManagerAndroid(context);
        ProductsList.setProductsList(productDBManagerAndroid.getProductsList());

        //Indicar en el primer parámetro el tipo de objetos y en el último parámetro el nombre de la lista obtenida antes
        setListAdapter(new ArrayAdapter<Product>(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                ProductsList.getProductsList()));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;

        //Guardar una referencia al contexto, ya que hará falta para crear la BD
        context = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

//        mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);
        //Enviar al fragment de detalle la posición del elemento que se ha pulsado
        Log.d(ProductListFragment.class.getName(), "Se ha detectado clic en el registro con posición: " + position);
        mCallbacks.onItemSelected(String.valueOf(position));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }
}

