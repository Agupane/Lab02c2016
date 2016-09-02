package ar.edu.utn.frsf.isi.dam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, AdapterView.OnItemClickListener, View.OnClickListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {
    private ToggleButton tbReservarMesa;
    private Spinner spinnerHorarios;
    private Switch switchReserva;
    private TextView tvDatosPedidos;
    private RadioGroup radioGroupBotones;
    private RadioButton rbPlato, rbPostre,rbBebida;
    private Button bAgregar,bconfirmarPedido,bReiniciar;
    private ListView lvPedidos;
    private ArrayAdapter<String> spinnerAdapter,listViewAdapter;
    private String[] horarios;
    private ArrayList<String> horariosArrayList;
    private double precioTotal = 0;

    private  ElementoMenu[] listaBebidas;
    private  ElementoMenu[] listaPlatos;
    private  ElementoMenu[] listaPostre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cargarVariables();
        horarios = new String[]{"20:00","20:30","21:00","21:30","22:00"};
        horariosArrayList = new ArrayList<String>();
        horariosArrayList.addAll(Arrays.asList(horarios));
        spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,horariosArrayList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHorarios.setAdapter(spinnerAdapter);
     //   listViewAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,);
        setearListeners();
        tvDatosPedidos.setMovementMethod(new ScrollingMovementMethod());
    }


    /**
     * Encuentra los componentes y los carga
     */
    private void cargarVariables()
    {
        bAgregar = (Button) findViewById(R.id.buttonAgregar);
        bconfirmarPedido = (Button) findViewById(R.id.buttonConfirmarPedido);
        bReiniciar = (Button) findViewById(R.id.buttonReiniciar);
        rbBebida = (RadioButton) findViewById(R.id.rbBebida);
        rbPlato = (RadioButton) findViewById(R.id.rbPlato);
        rbPostre = (RadioButton) findViewById(R.id.rbPostre);
        tvDatosPedidos = (TextView) findViewById(R.id.tvDatosPedidos);
        spinnerHorarios = (Spinner) findViewById(R.id.spinnerHorarios);
        tbReservarMesa = (ToggleButton) findViewById(R.id.tbReservarMesa);
        switchReserva = (Switch) findViewById(R.id.switchReserva);
        lvPedidos = (ListView) findViewById(R.id.listViewPedidos);
        radioGroupBotones = (RadioGroup) findViewById(R.id.radioGroupBotones);
    }
    /**
     * Agrega los listeners a cada componente
     */
    private void setearListeners()
    {
        tbReservarMesa.setOnClickListener(this);
        spinnerHorarios.setOnItemSelectedListener(this);
        switchReserva.setOnCheckedChangeListener(this);
        /*
        rbBebida.setOnCheckedChangeListener(this);
        rbPostre.setOnCheckedChangeListener(this);
        rbPlato.setOnCheckedChangeListener(this);
        */
        bAgregar.setOnClickListener(this);
        bconfirmarPedido.setOnClickListener(this);
        bReiniciar.setOnClickListener(this);
        radioGroupBotones.setOnCheckedChangeListener(this);

        lvPedidos.setOnItemSelectedListener(this);
    }


    /**
     * Setea adapters a los componentes
     */
    private void setearAdapters()
    {
        lvPedidos.setAdapter(listViewAdapter);
        spinnerHorarios.setAdapter(spinnerAdapter);
    }
    @Override
    /**
     * Captura el evento cuando un boton del rg es seleccionado
     */
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        ElementoMenu menu = ElementoMenu.getInstance();
        iniciarListas();
        switch(checkedId)
        {
            case -1:
            {
                break;
            }
            case R.id.rbBebida:
            {
                ArrayList<String> arrayBebidas = new ArrayList<>();
                for(ElementoMenu it : listaBebidas){
                    arrayBebidas.add(it.toString());
                }
                listViewAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,arrayBebidas);
                lvPedidos.setAdapter(listViewAdapter);

                break;

            }
            case R.id.rbPlato:
            {
                ArrayList<String> arrayPlato = new ArrayList<>();
                for(ElementoMenu it : listaPlatos){
                    arrayPlato.add(it.toString());
                }
                listViewAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,arrayPlato);
                lvPedidos.setAdapter(listViewAdapter);
                break;
            }
            case R.id.rbPostre:
            {
                ArrayList<String> arrayPostres = new ArrayList<>();
                for(ElementoMenu it : listaPostre){
                    arrayPostres.add(it.toString());
                }
                listViewAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,arrayPostres);
                lvPedidos.setAdapter(listViewAdapter);
                break;
            }
            default:
            {
                break;
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buttonAgregar:{

                SparseBooleanArray itemsSelect = lvPedidos.getCheckedItemPositions();
                for(int i=0 ; i<lvPedidos.getCount();i++){
                    if (itemsSelect.get(i)) {
                        String[] texto = ((String) lvPedidos.getItemAtPosition(i)).split("\\(");
                        String[] texto2 = texto[1].split("\\)");
                        String valor = texto2[0];
                        precioTotal += Double.parseDouble(valor);
                        tvDatosPedidos.append(((String) lvPedidos.getItemAtPosition(i)) + "\n");
                    }
                }
                lvPedidos.clearChoices();
                listViewAdapter.notifyDataSetChanged();
                break;
            }
            case R.id.buttonConfirmarPedido:{
                tvDatosPedidos.append("Costo Total: "+precioTotal);

                break;
            }
            case R.id.buttonReiniciar: {
                tvDatosPedidos.setText("");
                lvPedidos.clearChoices();
                listViewAdapter.notifyDataSetChanged();
                break;
            }
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }


    private void iniciarListas(){
        // inicia lista de bebidas
        listaBebidas = new ElementoMenu[7];
        listaBebidas[0]=new ElementoMenu(1,"Coca");
        listaBebidas[1]=new ElementoMenu(4,"Jugo");
        listaBebidas[2]=new ElementoMenu(6,"Agua");
        listaBebidas[3]=new ElementoMenu(8,"Soda");
        listaBebidas[4]=new ElementoMenu(9,"Fernet");
        listaBebidas[5]=new ElementoMenu(10,"Vino");
        listaBebidas[6]=new ElementoMenu(11,"Cerveza");
        // inicia lista de platos
        listaPlatos= new ElementoMenu[14];
        listaPlatos[0]=new ElementoMenu(1,"Ravioles");
        listaPlatos[1]=new ElementoMenu(2,"Gnocchi");
        listaPlatos[2]=new ElementoMenu(3,"Tallarines");
        listaPlatos[3]=new ElementoMenu(4,"Lomo");
        listaPlatos[4]=new ElementoMenu(5,"Entrecot");
        listaPlatos[5]=new ElementoMenu(6,"Pollo");
        listaPlatos[6]=new ElementoMenu(7,"Pechuga");
        listaPlatos[7]=new ElementoMenu(8,"Pizza");
        listaPlatos[8]=new ElementoMenu(9,"Empanadas");
        listaPlatos[9]=new ElementoMenu(10,"Milanesas");
        listaPlatos[10]=new ElementoMenu(11,"Picada 1");
        listaPlatos[11]=new ElementoMenu(12,"Picada 2");
        listaPlatos[12]=new ElementoMenu(13,"Hamburguesa");
        listaPlatos[13]=new ElementoMenu(14,"Calamares");
        // inicia lista de postres
        listaPostre= new ElementoMenu[15];
        listaPostre[0]=new ElementoMenu(1,"Helado");
        listaPostre[1]=new ElementoMenu(2,"Ensalada de Frutas");
        listaPostre[2]=new ElementoMenu(3,"Macedonia");
        listaPostre[3]=new ElementoMenu(4,"Brownie");
        listaPostre[4]=new ElementoMenu(5,"Cheescake");
        listaPostre[5]=new ElementoMenu(6,"Tiramisu");
        listaPostre[6]=new ElementoMenu(7,"Mousse");
        listaPostre[7]=new ElementoMenu(8,"Fondue");
        listaPostre[8]=new ElementoMenu(9,"Profiterol");
        listaPostre[9]=new ElementoMenu(10,"Selva Negra");
        listaPostre[10]=new ElementoMenu(11,"Lemon Pie");
        listaPostre[11]=new ElementoMenu(12,"KitKat");
        listaPostre[12]=new ElementoMenu(13,"IceCreamSandwich");
        listaPostre[13]=new ElementoMenu(14,"Frozen Yougurth");
        listaPostre[14]=new ElementoMenu(15,"Queso y Batata");

    }
}
