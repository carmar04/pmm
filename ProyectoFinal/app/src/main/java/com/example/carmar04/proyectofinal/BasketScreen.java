package com.example.carmar04.proyectofinal;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BasketScreen extends AppCompatActivity implements dialog_fragment_dismiss_product.OnDismissedProduct {

    ArrayList chosenProducts = new ArrayList();
    ArrayList <Product> chosenProduct = new ArrayList();
    User user;
    SQLSentences.DatabaseHelper dbHelper;
    ListView BasketListView;
    ProductAdapter productAdapter;
    TextView textTotalAmount;
    TextView textProductQuantity;
    Button ButtonPurchase;
    boolean checker = false;

    @Override
    public void dismissProduct(int position) {
        chosenProduct.remove(position);
        chosenProducts.remove(position);
        BasketListView.setAdapter(productAdapter);
        textTotalAmount.setText(String.valueOf(totalAmount(chosenProducts)));
        textProductQuantity.setText(productQuantity(chosenProducts));
        if(chosenProduct.size() == 0){
            ButtonPurchase.setEnabled(false);
        }else{
            ButtonPurchase.setEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_screen);
        Intent intent = getIntent();
        chosenProducts = intent.getParcelableArrayListExtra("ChosenProducts");
        user = (User) intent.getSerializableExtra("User");
        if(chosenProducts.size() > 0) {
            for (int i = 0; i < chosenProducts.size(); i++) {
                chosenProduct.add((Product) chosenProducts.get(i));
            }
        }
        TextView userOrder = findViewById(R.id.OrderUser);
        userOrder.setText(user.getNickName());




        textTotalAmount = findViewById(R.id.BasketTotalAmount);
        textProductQuantity = findViewById(R.id.BasketProductQuantity);
        textTotalAmount.setText(String.valueOf(totalAmount(chosenProduct)));
        textProductQuantity.setText(productQuantity(chosenProduct));

        dbHelper = new SQLSentences.DatabaseHelper(this);
        dbHelper.open();

        BasketListView = findViewById(R.id.BasketListView);
        productAdapter = new ProductAdapter(this);
        BasketListView.setAdapter(productAdapter);

        BasketListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialogDismiss(position);
            }
        });

        Button ButtonBack = findViewById(R.id.BasketButtonBack);
        ButtonPurchase = findViewById(R.id.BasketButtonPurchase);

        ButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BasketScreen.this, Pantalla3.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("UserRegistered", user);
                bundle.putParcelableArrayList("ChosenProducts", chosenProducts);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        if(chosenProduct.size() == 0){
            ButtonPurchase.setEnabled(false);
        }

        ButtonPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String [] userName = {user.getNickName()};
                int userId = 0;
                Cursor cursor;
                cursor = dbHelper.getItems("SELECT id FROM Users WHERE nickname=?", userName);

                if(cursor != null){
                    if(cursor.moveToFirst()){
                        do{
                            userId = cursor.getInt(0);
                        }while(cursor.moveToNext());

                    }

                }
                user.setUserId(userId);
                Toast.makeText(getApplicationContext(), String.valueOf(user.getUserId()), Toast.LENGTH_SHORT).show();
                dbHelper.insertItem("INSERT INTO Orders (user_id, articles, amount) VALUES" +
                        " ( "+ userId +" , " + chosenProduct.size() + " , " + totalAmount(chosenProducts) + " )");

                Cursor cursor2;
                int orderId = 0;
                cursor2 = dbHelper.getItems("SELECT id FROM Orders ORDER BY id DESC LIMIT 1", null);

                if(cursor2 != null){
                    if(cursor2.moveToFirst()){
                        do{
                            orderId = cursor2.getInt(0);
                            Toast.makeText(getApplicationContext(), "Pedidos: " + String.valueOf(orderId), Toast.LENGTH_SHORT).show();
                        }while(cursor2.moveToNext());
                    }
                }
                String sql = "";
                if(chosenProduct.size()==1){
                    sql = "( " + orderId + ", " + chosenProduct.get(0).getProductId() +" )";
                    dbHelper.insertItem("INSERT INTO OrderLines (order_id, product_id) VALUES " + sql);
                }else if(chosenProduct.size() > 1){
                    for(int i = 0; i < chosenProduct.size(); i++){
                        if(chosenProduct.size() - 1 == i){
                            sql += "( " + orderId + " , " + chosenProduct.get(i).getProductId() + " )";
                        }else{
                            sql += "( " + orderId + " , " + chosenProduct.get(i).getProductId() + " ), ";
                        }
                    }
                    dbHelper.insertItem("INSERT INTO OrderLines (order_id, product_id) VALUES " + sql);
                }else{
                    Toast.makeText(getApplicationContext(), "No hay articulos en la cesta para realizar un pedido", Toast.LENGTH_SHORT).show();
                }


                Intent intent = new Intent(BasketScreen.this, OrderScreen.class);
                Bundle bundle = new Bundle();
                ArrayList products = new ArrayList();
                bundle.putSerializable("User", user);
                bundle.putParcelableArrayList("ChosenProducts", products);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }



    public class ProductAdapter extends ArrayAdapter {
        Activity context;

        ProductAdapter(Activity context){
            super(context, R.layout.list_components, chosenProduct);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_components, parent, false);


            TextView productName = (TextView) convertView.findViewById(R.id.ProductName);
            TextView productStock = (TextView) convertView.findViewById(R.id.ProductStock);
            TextView productPrice = (TextView) convertView.findViewById(R.id.ProductPrice);
            ImageView productImage = (ImageView) convertView.findViewById(R.id.ProductImage);

            productName.setText(chosenProduct.get(position).getProductName());
            productStock.setText(chosenProduct.get(position).getProductStock());
            productPrice.setText(String.valueOf(chosenProduct.get(position).getProductPrice()));
            productImage.setImageDrawable(getDrawable(chosenProduct.get(position).getProductImage()));

            return convertView;
        }
    }
    public double totalAmount(ArrayList <Product> products){
        double totAmount = 0;
        for(int i = 0; i < products.size();i++){
            totAmount += products.get(i).getProductPrice();
        }
        return totAmount;
    }
    public String productQuantity(ArrayList <Product> products){
        String quantity = "";
        if(products.size() == 1){
            quantity = "1 articulo";
        }else if(products.size() > 1){
            quantity = products.size() + " articulos";
        }else{
            quantity = "0 articulos";
        }
        return quantity;
    }
    void showDialogDismiss(int position) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("NewPurchase");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment dialogFragment = dialog_fragment_dismiss_product.newInstance(position);
        dialogFragment.show(ft,"NewPurchase");
    }
}
