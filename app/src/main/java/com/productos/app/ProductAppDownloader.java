package com.productos.app;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Solari on 23/05/14.
 */
public class ProductAppDownloader extends AsyncTask<String, Void, Void> {
    private final String NAMESPACE = null;
    private XmlPullParser parser = Xml.newPullParser();

    private static final String TAG_XML = "products";
    private static final String TAG_REGISTRO = "product";
    private ArrayList<Product> listaDatos;
    private Context context;
    private ProductListFragment productListFragment;

    public ProductAppDownloader(Context context, ProductListFragment senderoListFragment) {
        this.context = context;
        this.productListFragment = senderoListFragment;
    }

    private Product leerRegistro() {
        //Crear una variable para cada dato del objeto
        int id = -1;
        String name = "";
        String category = "";
        String price = "";
        String comments = "";
        String photoFileName = "";
        try {
            parser.require(XmlPullParser.START_TAG, NAMESPACE, TAG_REGISTRO);
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String tagName = parser.getName();
                if (tagName.equals("id")) {
                    id = Integer.valueOf(readText(tagName));
                } else if (tagName.equals("name")) {
                    name = readText(tagName);
                } else if (tagName.equals("category")) {
                    category = readText(tagName);
                } else if (tagName.equals("price")) {
                    price = readText(tagName);
                } else if (tagName.equals("comments")) {
                    comments = readText(tagName);
                } else if (tagName.equals("photo_file_name")) {
                    photoFileName = readText(tagName);
                } else {
                    skip();
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Product product = new Product(id, name, category, price, comments, photoFileName);
        return product;
    }


    protected void onPostExecute(Void aVoid) {
        Log.d(ProductAppDownloader.class.getName(), "Descarga de datos finalizada. Iniciando procesamiento");
        Log.d(ProductAppDownloader.class.getName(), "Nº registros descargados: "+listaDatos.size());
        //Conectar con la BD y recorrer los elementos descargados desde el XML
        ProductDBManagerAndroid productDBManagerAndroid = new ProductDBManagerAndroid(context);
        for (Product product : listaDatos) {
            //Comprobar si ya existe un elemento con la misma ID
            Log.d(ProductAppDownloader.class.getName(), "Comprobando si exite el id: "+product.getId());
            if (productDBManagerAndroid.getProductByID(product.getId()) == null) {
                Log.d(ProductAppDownloader.class.getName(), "Id no encontrado. Se insertará el registro");
                //Si no existe, se inserta
                productDBManagerAndroid.insertProduct(product);
            } else {
                Log.d(ProductAppDownloader.class.getName(), "Id existente. Se actualizará el registro");
                //Ya existe un contacto con ese id, se actualiza con los datos descargados
                productDBManagerAndroid.updateProduct(product);
            }
        }

        //Borrar los contactos del teléfono que no estén en el XML descargado
        ArrayList<Product> productsListDB = productDBManagerAndroid.getProductsList();
        //Se recorre cada persona de la BD local comprobando si existen en los datos del documento XML
        for (Product product : productsListDB) {
            //Para comparar si dos personas son iguales se ha sobrecargado el método equals en la clase Person
            if(listaDatos.indexOf(product)==-1) {
                Log.d(ProductDBManagerAndroid.class.getName(), "Producto a eliminar: " +product.toString());
                //Si se observa que no está en el XML, se elimina de la BD
                productDBManagerAndroid.deleteProductById(product.getId());
            }
        }

        //Mostrar la lista una vez finalizada la descarga
        productListFragment.showProductList();
    }


    protected Void doInBackground(String... urls) {
        Log.d(ProductAppDownloader.class.getName(), "Iniciando descarga de datos en segundo plano");
        InputStream stream = null;
        try {
            Log.d(ProductAppDownloader.class.getName(), "Dirección de descarga: "+urls[0]);
            stream = downloadUrl(urls[0]);
            listaDatos = xmlToList(stream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }


    private ArrayList xmlToList(InputStream stream) {
        Log.d(ProductAppDownloader.class.getName(), "Iniciando interpretación de datos XML");
        ArrayList list = new ArrayList();
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(stream, null);
            parser.nextTag();
            Log.d(ProductAppDownloader.class.getName(), "Primera etiqueta encontrada: "+parser.getName());
            parser.require(XmlPullParser.START_TAG, NAMESPACE, TAG_XML);
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                Log.d(ProductAppDownloader.class.getName(), "Etiqueta encontrada: "+parser.getName());
                if (name.equals(TAG_REGISTRO)) {
                    list.add(leerRegistro());
                } else {
                    skip();
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void skip() throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private String readText(String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, tag);
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, NAMESPACE, tag);
        return result;
    }



}
