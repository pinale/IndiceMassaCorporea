package com.example.apinato.indicemassacorporea;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    String sesso;
    int iPeso;
    int iAltezza;

    TextView vPeso;// = (TextView) findViewById(R.id.txtPeso);
    TextView vAltezza;// = (TextView) findViewById(R.id.txtAltezza);

    //http://stackoverflow.com/questions/7938516/continuously-increase-integer-value-as-the-button-is-pressed
    private Handler repeatUpdateHandler = new Handler();
    private boolean mAutoIncrementPeso = false;
    private boolean mAutoDecrementPeso = false;
    private boolean mAutoIncrementAltezza = false;
    private boolean mAutoDecrementAltezza = false;
    public int mValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //occhio tolto il final (se non funziona decentralizzare tutto e ridichiarare sul posto le view)
        vPeso = (TextView) findViewById(R.id.txtPeso);
        vAltezza = (TextView) findViewById(R.id.txtAltezza);


        //inizializzazione
        sesso = "M";
        iPeso = 70;
        iAltezza = 170;

        //registro i listener degli eventi
        Button btnAltezzaMeno = (Button) findViewById(R.id.btnaltezzameno);
        Button btnAltezzaPiu = (Button) findViewById(R.id.btnaltezzapiu);
        Button btnPesoMeno = (Button) findViewById(R.id.btnpesomeno);
        Button btnPesoPiu = (Button) findViewById(R.id.btnpesopiu);
        RadioButton optM = (RadioButton) findViewById(R.id.optSessoM);
        RadioButton optF = (RadioButton) findViewById(R.id.optSessoF);
        //simple click
        btnAltezzaMeno.setOnClickListener(myButtonListener);
        btnAltezzaPiu.setOnClickListener(myButtonListener);
        btnPesoMeno.setOnClickListener(myButtonListener);
        btnPesoPiu.setOnClickListener(myButtonListener);
        optM.setOnClickListener(myButtonListener);
        optF.setOnClickListener(myButtonListener);


        //=====long press PESO=======
        btnPesoPiu.setOnLongClickListener(
                new View.OnLongClickListener() {
                    public boolean onLongClick(View arg0) {
                        mAutoIncrementPeso = true;
                        repeatUpdateHandler.post(new RptUpdater());
                        return false;
                    }
                }
        );

        btnPesoPiu.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                        && mAutoIncrementPeso) {
                    mAutoIncrementPeso = false;
                }
                return false;
            }
        });

        btnPesoMeno.setOnLongClickListener(
                new View.OnLongClickListener() {
                    public boolean onLongClick(View arg0) {
                        mAutoDecrementPeso = true;
                        repeatUpdateHandler.post(new RptUpdater());
                        return false;
                    }
                }
        );

        btnPesoMeno.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                        && mAutoDecrementPeso) {
                    mAutoDecrementPeso = false;
                }
                return false;
            }
        });


        //verificare
        //=====long press ALTEZZA=======
        btnAltezzaPiu.setOnLongClickListener(
                new View.OnLongClickListener() {
                    public boolean onLongClick(View arg0) {
                        mAutoIncrementAltezza = true;
                        repeatUpdateHandler.post(new RptUpdater());
                        return false;
                    }
                }
        );

        btnAltezzaPiu.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                        && mAutoIncrementAltezza) {
                    mAutoIncrementAltezza = false;
                }
                return false;
            }
        });

        btnAltezzaMeno.setOnLongClickListener(
                new View.OnLongClickListener() {
                    public boolean onLongClick(View arg0) {
                        mAutoDecrementAltezza = true;
                        repeatUpdateHandler.post(new RptUpdater());
                        return false;
                    }
                }
        );

        btnAltezzaMeno.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                        && mAutoDecrementAltezza) {
                    mAutoDecrementAltezza = false;
                }
                return false;
            }
        });

        calcolaIMC(iPeso, iAltezza, sesso);
    }
    ///fine da verificare

    //simple click event
    private OnClickListener myButtonListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            //final TextView peso = (TextView) findViewById(R.id.txtPeso);
            //final TextView altezza = (TextView) findViewById(R.id.txtAltezza);

            //int iPeso = Integer.parseInt(peso.getText().toString());
            //int iAltezza = Integer.parseInt(altezza.getText().toString());

            switch (view.getId()) {
                case R.id.btnaltezzameno:
                    if (iAltezza > 100)
                        iAltezza--;
                    break;
                case R.id.btnaltezzapiu:
                    if (iAltezza < 230)
                        iAltezza++;
                    break;
                case R.id.btnpesomeno:
                    if (iPeso > 40)
                        iPeso--;
                    break;
                case R.id.btnpesopiu:
                    if (iPeso < 250)
                        iPeso++;
                    break;
                //option
                case R.id.optSessoF:
                    sesso = "F";
                    break;
                case R.id.optSessoM:
                    sesso = "M";
                    break;
            }

            //final TextView peso = (TextView) findViewById(R.id.txtPeso);
            //final TextView altezza = (TextView) findViewById(R.id.txtAltezza);
            vAltezza.setText(Integer.toString(iAltezza));
            vPeso.setText(Integer.toString(iPeso));

            calcolaIMC(iPeso, iAltezza, sesso);
        }
    };

    public void calcolaIMC(float peso, float altezza, String sesso) {

        float imc = peso / ((altezza / 100) * (altezza / 100));
        ImageView imgPeso = (ImageView) findViewById(R.id.imgResult);
        TextView txtOnImage = (TextView) findViewById(R.id.myImageViewText);

        txtOnImage.setText(String.format("%.1f", imc));

        //setto l'immagine in base al valore (range)
        if (sesso == "M") {
            if (imc < 18.5) {
                imgPeso.setImageResource(R.drawable.uomosottopeso);
            } else if (imc >= 18.5 && imc <= 24.999999) {
                imgPeso.setImageResource(R.drawable.uomonormale);
            } else if (imc > 24.999999 && imc <= 30.0) {
                imgPeso.setImageResource(R.drawable.uomosovrappeso);
            } else if (imc > 30.0) {
                imgPeso.setImageResource(R.drawable.uomoobeso);
            }
        } else if (sesso == "F") {
            if (imc < 18.5) {
                imgPeso.setImageResource(R.drawable.donnasottopeso);
            } else if (imc >= 18.5 && imc <= 24.999999) {
                imgPeso.setImageResource(R.drawable.donnanormale);
            } else if (imc > 24.999999 && imc <= 30.0) {
                imgPeso.setImageResource(R.drawable.donnasovrappeso);
            } else if (imc > 30.0) {
                imgPeso.setImageResource(R.drawable.donnaobesa);
            }
        }
    }

    public void incrementPeso() {
        if (iPeso < 250)
            iPeso++;
        //final TextView peso = (TextView) findViewById(R.id.txtPeso);
        vPeso.setText("" + iPeso);
    }

    public void decrementPeso() {
        if (iPeso > 40)
            iPeso--;
        //final TextView peso = (TextView) findViewById(R.id.txtPeso);
        vPeso.setText("" + iPeso);
    }

    public void incrementAltezza() {
        if (iAltezza < 230)
            iAltezza++;
        //final TextView peso = (TextView) findViewById(R.id.txtPeso);
        vAltezza.setText("" + iAltezza);
    }

    public void decrementAltezza() {
        if (iAltezza > 100)
            iAltezza--;
        //final TextView peso = (TextView) findViewById(R.id.txtPeso);
        vAltezza.setText("" + iAltezza);
    }


    //class runnable per il longpress
    class RptUpdater implements Runnable {
        long REP_DELAY = 100;

        public void run() {
            if (mAutoIncrementPeso) {
                incrementPeso();
                repeatUpdateHandler.postDelayed(new RptUpdater(), REP_DELAY);
            } else if (mAutoDecrementPeso) {
                decrementPeso();
                repeatUpdateHandler.postDelayed(new RptUpdater(), REP_DELAY);
            }

            //verificare
            if (mAutoIncrementAltezza) {
                incrementAltezza();
                repeatUpdateHandler.postDelayed(new RptUpdater(), REP_DELAY);
            } else if (mAutoDecrementAltezza) {
                decrementAltezza();
                repeatUpdateHandler.postDelayed(new RptUpdater(), REP_DELAY);
            }
        }
    }
}
