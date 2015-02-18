package com.andreamontanari.mythesis;

/**
 * Created by andreamontanari on 13/02/15.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

//classe custom per la finestra di caricamento
public class LoadingView extends RelativeLayout {

    public LoadingView(Context context) {
        super(context);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
        init();
    }

    //carica il contenuto statico della pagina contenuto in res/layout/loadin.xml
    private void init() {
        inflate(getContext(), R.layout.loading, this);
    }
}
