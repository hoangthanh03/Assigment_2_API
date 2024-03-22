package lab.poly.demoasm_and103;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity {
    RecyclerView rccFruit;
    List<fruitModel> listFruit;
    fruitAdapter fruitAdapter;
    Button btnAdd;

    //
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(APIservice.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    APIservice apiService = retrofit.create(APIservice.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //
        rccFruit = findViewById(R.id.rccFruit);
        btnAdd = findViewById(R.id.btnAdd);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rccFruit.setLayoutManager(layoutManager);
        listFruit = new ArrayList<>();
        fruitAdapter = new fruitAdapter(this, listFruit);
        rccFruit.setAdapter(fruitAdapter);


        Call<List<fruitModel>> call = apiService.getfruitList();
        call.enqueue(new Callback<List<fruitModel>>() {
            @Override
            public void onResponse(Call<List<fruitModel>> call, Response<List<fruitModel>> response) {
                if (response.isSuccessful()) {
                    List<fruitModel> fetchedFruits = response.body();
                    if (fetchedFruits != null) {
                        listFruit.clear(); // Xóa dữ liệu cũ
                        listFruit.addAll(fetchedFruits); // Thêm dữ liệu mới
                        fruitAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                    }
                } else {
                    Log.e("Home", "Failed to get fruits: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<fruitModel>> call, Throwable t) {
                Log.e("Home", t.getMessage());
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddStudentDialog();
            }
        });

    }

    private void showAddStudentDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add, null);
        dialogBuilder.setView(dialogView);

        TextInputEditText edtName = dialogView.findViewById(R.id.edtName);
        TextInputEditText edtPrice = dialogView.findViewById(R.id.edtPrice);
        TextInputEditText edtOrigin = dialogView.findViewById(R.id.edtOrigin);
        TextInputEditText edtQuantity = dialogView.findViewById(R.id.edtQuantity);
        Button btnAdd = dialogView.findViewById(R.id.btnAdd_add);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel_add);
        AlertDialog alertDialog = dialogBuilder.create();
        TextInputLayout errName = dialogView.findViewById(R.id.errName);
        TextInputLayout errPrice = dialogView.findViewById(R.id.errPrice);
        TextInputLayout errOrigin = dialogView.findViewById(R.id.errOrigin);
        TextInputLayout errQuantity = dialogView.findViewById(R.id.errQuantity);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                String price = edtPrice.getText().toString();
                String origin = edtOrigin.getText().toString();
                String quantity = edtQuantity.getText().toString();
                String img = "https://i.pinimg.com/236x/2e/4d/db/2e4ddb3c2ca38d6116abba5b663bac6e.jpg";

                if (name.isEmpty() && price.isEmpty() && origin.isEmpty() && quantity.isEmpty()) {
                    errName.setError("Name is required");
                    errPrice.setError("Price is required");
                    errOrigin.setError("Origin is required");
                    errQuantity.setError("Quantity is required");
                    return;
                }
                if(name.isEmpty()){
                    errName.setError("Name is required");
                    return;
                }else{
                    errName.setError(null);
                }
                if(price.isEmpty()){
                    errPrice.setError("Price is required");
                    return;
                }else{
                    errPrice.setError(null);
                }
                if(origin.isEmpty()){
                    errOrigin.setError("Origin is required");
                    return;
                }else{
                    errOrigin.setError(null);
                }
                if(quantity.isEmpty()){
                    errQuantity.setError("Quantity is required");
                    return;
                }else{
                    errQuantity.setError(null);
                }

                //giá phải là số
                if(!price.isEmpty()){
                    try{
                        Double pri = Double.parseDouble(price);
                    }catch(NumberFormatException e){
                        errPrice.setError("Price must be a number");
                        return;
                    }
                }

                //sol：
                if(!quantity.isEmpty()){
                    try{
                        int qty = Integer.parseInt(quantity);
                    }catch(NumberFormatException e){
                        errQuantity.setError("Quantity must be a number");
                        return;
                    }
                }

                try{
                    Double pri = Double.parseDouble(price);
                    int qty = Integer.parseInt(quantity);

                    fruitModel newFruit = new fruitModel(name, pri, origin, img, qty);
                    Call<fruitModel> call = apiService.addFruit(newFruit);
                    call.enqueue(new Callback<fruitModel>() {
                        @Override
                        public void onResponse(Call<fruitModel> call, Response<fruitModel> response) {
                            if (response.isSuccessful()) {
                                // Xử lý thành công khi nhận được phản hồi từ máy chủ
                                fruitModel addedFruit = response.body();
                                // Thêm sinh viên mới vào danh sách hiện tại hoặc làm các thao tác cần thiết khác
                                listFruit.add(addedFruit);
                                // Sau đó cập nhật RecyclerView để hiển thị sinh viên mới
                                fruitAdapter.notifyDataSetChanged();
                                alertDialog.dismiss();

                            } else {
                                // Xử lý khi nhận được phản hồi lỗi từ máy chủ
                                Log.e("Add Fruit", "Failed to add fruit: " + response.message());
                                // Hiển thị thông báo lỗi cho người dùng nếu cần thiết
                            }
                        }

                        @Override
                        public void onFailure(Call<fruitModel> call, Throwable t) {
                            // Xử lý khi gặp lỗi trong quá trình gửi yêu cầu
                            Log.e("Add Fruit", "Error adding fruit: " + t.getMessage());
                            // Hiển thị thông báo lỗi cho người dùng nếu cần thiết
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}