package lab.poly.demoasm_and103;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import lab.poly.demoasm_and103.adapter.FruitAdapter;
import lab.poly.demoasm_and103.models.Fruit;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity implements IClick {
    File file;
    RecyclerView rccFruit;
    List<Fruit> listFruit;
    FruitAdapter fruitAdapter;
    Button btnAdd;
    ImageView imgFruit_add;
    ImageView imgFruit_ud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //
        rccFruit = findViewById(R.id.rccFruit);
        btnAdd = findViewById(R.id.btnAdd);
        EditText edtSearch = findViewById(R.id.edtSearch);
        Button btnTang = findViewById(R.id.btnTang);
        Button btnGiam = findViewById(R.id.btnGiam);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rccFruit.setLayoutManager(layoutManager);
        listFruit = new ArrayList<>();
        fruitAdapter = new FruitAdapter(this, listFruit, this);
        rccFruit.setAdapter(fruitAdapter);


        getListFruit();

        btnTang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(APIservice.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
                APIservice apiService = retrofit.create(APIservice.class);
                Call<List<Fruit>> call = apiService.sxepTang();
                call.enqueue(new Callback<List<Fruit>>() {
                    @Override
                    public void onResponse(Call<List<Fruit>> call, Response<List<Fruit>> response) {
                        if (response.isSuccessful()) {
                            listFruit.clear();
                            listFruit.addAll(response.body());
                            fruitAdapter = new FruitAdapter(Home.this, listFruit, Home.this);
                            rccFruit.setAdapter(fruitAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Fruit>> call, Throwable t) {
                        Log.e("Home", t.getMessage());
                    }
                });
            }
        });
        btnGiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(APIservice.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
                APIservice apiService = retrofit.create(APIservice.class);
                Call<List<Fruit>> call = apiService.sxepGiam();
               call.enqueue(new Callback<List<Fruit>>() {
                   @Override
                   public void onResponse(Call<List<Fruit>> call, Response<List<Fruit>> response) {
                       if (response.isSuccessful()) {
                           listFruit.clear();
                           listFruit.addAll(response.body());
                           fruitAdapter = new FruitAdapter(Home.this, listFruit, Home.this);
                           rccFruit.setAdapter(fruitAdapter);
                       }
                   }

                   @Override
                   public void onFailure(Call<List<Fruit>> call, Throwable t) {

                   }
               });
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String key = charSequence.toString();
                if (key.length() == 0 && key.contains("")) {
                    getListFruit();
                }
                if (!key.isEmpty()) {
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(APIservice.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
                    APIservice apiService = retrofit.create(APIservice.class);
                    Call<List<Fruit>> call = apiService.searchFruit(key);
                    call.enqueue(new Callback<List<Fruit>>() {
                        @Override
                        public void onResponse(Call<List<Fruit>> call, Response<List<Fruit>> response) {
                            if (response.isSuccessful()) {
                                listFruit.clear();
                                listFruit.addAll(response.body());

                                fruitAdapter = new FruitAdapter(Home.this, listFruit, Home.this);
                                rccFruit.setAdapter(fruitAdapter);

                                Log.d("Home", "Search successful");
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Fruit>> call, Throwable t) {

                            Log.e("Home", t.getMessage());
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAdd();
            }
        });

    }

    private void showDialogAdd() {
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
        imgFruit_add = dialogView.findViewById(R.id.imgFruit_add);

        imgFruit_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImageAdd();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                String price = edtPrice.getText().toString();
                String origin = edtOrigin.getText().toString();
                String quantity = edtQuantity.getText().toString();

                if (name.isEmpty() && price.isEmpty() && origin.isEmpty() && quantity.isEmpty()) {
                    errName.setError("Name is required");
                    errPrice.setError("Price is required");
                    errOrigin.setError("Origin is required");
                    errQuantity.setError("Quantity is required");
                    return;
                }
                if (name.isEmpty()) {
                    errName.setError("Name is required");
                    return;
                } else {
                    errName.setError(null);
                }
                if (price.isEmpty()) {
                    errPrice.setError("Price is required");
                    return;
                } else {
                    errPrice.setError(null);
                }
                if (origin.isEmpty()) {
                    errOrigin.setError("Origin is required");
                    return;
                } else {
                    errOrigin.setError(null);
                }
                if (quantity.isEmpty()) {
                    errQuantity.setError("Quantity is required");
                    return;
                } else {
                    errQuantity.setError(null);
                }

                //giá phải là số
                if (!price.isEmpty()) {
                    try {
                        Double pri = Double.parseDouble(price);
                    } catch (NumberFormatException e) {
                        errPrice.setError("Price must be a number");
                        return;
                    }
                }

                //sol：
                if (!quantity.isEmpty()) {
                    try {
                        int qty = Integer.parseInt(quantity);
                    } catch (NumberFormatException e) {
                        errQuantity.setError("Quantity must be a number");
                        return;
                    }
                }

                RequestBody namerq = RequestBody.create(MediaType.parse("multipart/form-data"), name);
                RequestBody pricerq = RequestBody.create(MediaType.parse("multipart/form-data"), price);
                RequestBody originrq = RequestBody.create(MediaType.parse("multipart/form-data"), origin);
                RequestBody quantityrq = RequestBody.create(MediaType.parse("multipart/form-data"), quantity);
                MultipartBody.Part multipartBody = null;

                if (file != null) {
                    RequestBody requesrFile = RequestBody.create(MediaType.parse("image/*"), file);
                    multipartBody = MultipartBody.Part.createFormData("image", file.getName(), requesrFile);
                    //"avatar" là cùng tên với key trong mutipart
                } else {
                    multipartBody = null;
                }
                Retrofit retrofit = new Retrofit.Builder().baseUrl(APIservice.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
                APIservice apiService = retrofit.create(APIservice.class);
                Call<Fruit> call = apiService.addFruit(namerq, pricerq, originrq, multipartBody, quantityrq);
                call.enqueue(new Callback<Fruit>() {
                    @Override
                    public void onResponse(Call<Fruit> call, Response<Fruit> response) {
                        if (response.isSuccessful()) {
                            Log.d("Add", "Add success");
                            getListFruit();
                            alertDialog.dismiss();
                            file = null;
                            alertDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<Fruit> call, Throwable t) {
                        Log.d("Add", "Add fail");
                    }
                });
            }
        });
        alertDialog.show();
    }

    @Override
    public void onEditClick(int position) {
        Fruit fruit = listFruit.get(position);
        if (position != RecyclerView.NO_POSITION) {
            // Gọi phương thức xóa của Adapter khi người dùng chọn xóa
            showUpdateDialog(fruit);
            Toast.makeText(Home.this, "Cập nhật", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeleteClick(int position) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIservice.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.d("Delete Fruit", "Deleting fruit at position: " + position);
        //tạo interface
        APIservice apiService = retrofit.create(APIservice.class);
        // Gọi phương thức Retrofit để thực hiện yêu cầu DELETE tới máy chủ
        Fruit fruitToDelete = listFruit.get(position);
        Call<Void> call = apiService.deleteFruit(fruitToDelete.get_id());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Xóa hoa khỏi danh sách và cập nhật RecyclerView
                    listFruit.remove(position);
                    fruitAdapter.notifyDataSetChanged();
                    Toast.makeText(Home.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                } else {
                    // Xử lý khi nhận được phản hồi lỗi từ máy chủ
                    Log.e("Delete", "Failed to delete: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý khi gặp lỗi trong quá trình gửi yêu cầu
                Log.e("Delete", "Error deleting: " + t.getMessage());
            }
        });
    }

    private void getListFruit() {
        Gson gson = new GsonBuilder().setLenient().create();

        //khởi tạo retrofit Clinet
        Retrofit retrofit = new Retrofit.Builder().baseUrl(APIservice.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        //tạo interface
        APIservice apiService = retrofit.create(APIservice.class);
        //tạo đối tượng Call
        Call<List<Fruit>> objCall = apiService.getfruitList();
        objCall.enqueue(new Callback<List<Fruit>>() {
            @Override
            public void onResponse(Call<List<Fruit>> call, Response<List<Fruit>> response) {
                if (response.isSuccessful()) {
                    listFruit.clear();
                    listFruit.addAll(response.body());
                    fruitAdapter.notifyDataSetChanged();
                    Log.e("Home", "Success");
                }
            }

            @Override
            public void onFailure(Call<List<Fruit>> call, Throwable t) {

                Log.e("Home", t.getMessage());
            }
        });
    }

    private void chooseImageAdd() {
        if (ContextCompat.checkSelfPermission(Home.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            getImageAdd.launch(intent);

        } else {
            ActivityCompat.requestPermissions(Home.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    //Hàm chọn hình
    private void chooseImageUpdate() {
        if (ContextCompat.checkSelfPermission(Home.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            getImageUpdate.launch(intent);

        } else {
            ActivityCompat.requestPermissions(Home.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }


    //hàm kết quả sau khi lấy hình
    ActivityResultLauncher<Intent> getImageAdd = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == RESULT_OK) {
                        Uri path = o.getData().getData();
                        file = createFileFormUri(path, "image");
                        //Glide để load hình
                        Glide.with(Home.this).load(file).thumbnail(Glide.with(Home.this).load(R.drawable.ing)).centerCrop()//ceter cắt ảnh
                                .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imgFruit_add);
                    }
                }
            }
    );
    ActivityResultLauncher<Intent> getImageUpdate = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == RESULT_OK) {
                        Uri path = o.getData().getData();
                        file = createFileFormUri(path, "image");
                        //Glide để load hình
                        Glide.with(Home.this).load(file).thumbnail(Glide.with(Home.this).load(R.drawable.ing)).centerCrop()//ceter cắt ảnh
                                .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imgFruit_ud);

                    }
                }
            }
    );

    //hàm tạo file hình từ uri
    private File createFileFormUri(Uri path, String name) {
        File _file = new File(Home.this.getCacheDir(), name + ".png");
        try {
            InputStream in = Home.this.getContentResolver().openInputStream(path);
            OutputStream out = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            return _file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void showUpdateDialog(Fruit fruit) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Home.this);

        // Inflate layout cho dialog
        LayoutInflater Inflater = LayoutInflater.from(this);
        View view = Inflater.from(this).inflate(R.layout.dialog_update, null);
        builder.setView(view);
        androidx.appcompat.app.AlertDialog dialog = builder.create();

        // Khởi tạo EditText và gán giá trị hiện tại của sản phẩm vào
        EditText editTextName = view.findViewById(R.id.edtName_up);
        EditText editTextPrice = view.findViewById(R.id.edtPrice_up);
        EditText editTextOrigin = view.findViewById(R.id.edtOrigin_up);
        EditText editTextQuantity = view.findViewById(R.id.edtQuantity_up);
        Button btnUpdate = view.findViewById(R.id.btnUpdate);
        Button btnCancel = view.findViewById(R.id.btnCancel_up);
        imgFruit_ud = view.findViewById(R.id.imgFruit_ud);


        editTextName.setText(fruit.getName());
        editTextPrice.setText("" + fruit.getPrice());
        editTextOrigin.setText(fruit.getOrigin());
        editTextQuantity.setText("" + fruit.getQuantity());

        Glide.with(this).load(fruit.getImage()).into(imgFruit_ud);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        imgFruit_ud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImageUpdate();
            }
        });
        // Xử lý sự kiện khi nhấn nút cập nhật trong dialog
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), editTextName.getText().toString());
                RequestBody price = RequestBody.create(MediaType.parse("multipart/form-data"), editTextPrice.getText().toString());
                RequestBody origin = RequestBody.create(MediaType.parse("multipart/form-data"), editTextOrigin.getText().toString());
                RequestBody quantity = RequestBody.create(MediaType.parse("multipart/form-data"), editTextQuantity.getText().toString());

                MultipartBody.Part multipartBody = null;
                if (file != null) {
                    RequestBody requesrFile = RequestBody.create(MediaType.parse("image/*"), file);
                    multipartBody = MultipartBody.Part.createFormData("image", file.getName(), requesrFile);
                    //"image" là cùng tên với key trong mutipart
                } else {
                    multipartBody = null;
                }

                // Tạo đối tượng được cập nhật
                Fruit updateFruit = new Fruit();
                updateFruit.setName(editTextName.getText().toString());
                updateFruit.setPrice(Double.parseDouble(editTextPrice.getText().toString()));
                updateFruit.setOrigin(editTextOrigin.getText().toString());
                updateFruit.setQuantity(Integer.parseInt(editTextQuantity.getText().toString()));

                Retrofit retrofit = new Retrofit.Builder().baseUrl(APIservice.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
                APIservice apiService = retrofit.create(APIservice.class);
                Call<Fruit> call = apiService.updateFruit(fruit.get_id(), name, price, origin, multipartBody, quantity);

                call.enqueue(new Callback<Fruit>() {
                    @Override
                    public void onResponse(Call<Fruit> call, Response<Fruit> response) {
                        if (response.isSuccessful()) {
                            // Cập nhật thành công
                            Call<List<Fruit>> call1 = apiService.getfruitList();
                            call1.enqueue(new Callback<List<Fruit>>() {
                                @Override
                                public void onResponse(Call<List<Fruit>> call1, Response<List<Fruit>> response) {
                                    if (response.isSuccessful()) {
                                        List<Fruit> fetchedFlowers = response.body();
                                        if (fetchedFlowers != null) {
                                            listFruit.clear(); // Xóa dữ liệu cũ
                                            listFruit.addAll(fetchedFlowers); // Thêm dữ liệu mới
                                            fruitAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                                            dialog.dismiss();
                                            file = null;
                                        }
                                    } else {
                                        Log.e("Home", "Failed to get flower: " + response.message());
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<Fruit>> call, Throwable t) {
                                    Log.e("Home", t.getMessage());
                                }
                            });

                        } else {
                            Toast.makeText(Home.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<Fruit> call, Throwable t) {
                        // Xử lý khi gữp lỗi trong quá trình gửi yêu cầu
                        Log.e("Update flower", "Error updating flower: " + t.getMessage());
                    }
                });
            }
        });
        // Hiển thị dialog
        dialog.show();
    }

}