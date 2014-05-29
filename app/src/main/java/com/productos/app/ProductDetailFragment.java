package com.productos.app;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Solari on 23/05/14.
 */
public class ProductDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    private ImageView imageViewPhoto;

    //    private DummyContent.DummyItem mItem;
    private Product product;

    public ProductDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
//            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            //Obtener la persona que se encuentre en la posición que se ha seleccionado dentro de la lista
            int productIndex = Integer.valueOf(getArguments().getString(ARG_ITEM_ID));
            product = ProductsList.getProductsList().get(productIndex);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_detail, container, false);

//        if (mItem != null) {
//            ((TextView) rootView.findViewById(R.id.person_detail)).setText(mItem.content);
//        }
        if(product != null) {
            ((EditText) rootView.findViewById(R.id.editTextName)).setText(product.getName());
            ((EditText) rootView.findViewById(R.id.editTextCategory)).setText(product.getCategory());
            ((EditText) rootView.findViewById(R.id.editTextPrice)).setText(product.getPrice());
            ((EditText) rootView.findViewById(R.id.editTextComments)).setText(product.getComments());
            //Descargar foto y mostrarla al finalizar la descarga, sólo si hay un nombre de archivo para la imagen
            if(!product.getPhotoFileName().trim().isEmpty() || product.getPhotoFileName()!=null) {
                imageViewPhoto = ((ImageView)rootView.findViewById(R.id.imageViewPhoto));
                ImageDownloader imageDownloader = new ImageDownloader();
                imageDownloader.execute(ProductListFragment.URL_IMAGES + product.getPhotoFileName());
            }
        }

        return rootView;
    }



    private class ImageDownloader extends AsyncTask<String, Void, Void> {

        private Bitmap bitmap;

        @Override
        protected Void doInBackground(String... strings) {
            String urlImage = strings[0];
            bitmap = getImageBitmap(urlImage);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            imageViewPhoto.setImageBitmap(bitmap);
        }

        private Bitmap getImageBitmap(String url) {
            Bitmap bm = null;
            try {
                URL aURL = new URL(url);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
            } catch (IOException e) {
                Log.e(ImageDownloader.class.getName(), "Error getting bitmap", e);
            }
            return bm;
        }
    }
}
