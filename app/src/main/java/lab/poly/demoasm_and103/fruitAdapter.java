package lab.poly.demoasm_and103;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class fruitAdapter extends RecyclerView.Adapter<fruitAdapter.ViewHolder> {
    private Context context;
    private List<fruitModel> listFruit;

    public fruitAdapter(Context context, List<fruitModel> listFruit) {
        this.context = context;
        this.listFruit = listFruit;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_fruit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText("Tên: " + listFruit.get(position).getName());
        holder.txtPrice.setText("Giá: " + listFruit.get(position).getPrice());
        holder.txtQuantity.setText("Số lượng: " + listFruit.get(position).getQuantity());
        holder.txtOrigin.setText("Xuât xứ: " + listFruit.get(position).getOrigin());
//        holder.ivImage.setImageResource(listFruit.get(position).getImage());
        String img = listFruit.get(position).getImage();
        if (img != null) {
            Glide.with(context).load(img).into(holder.ivImage);
        }

        holder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //comfirm Delete
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xóa");
                builder.setMessage("Bạn muôn xóa");


                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int position = holder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // Gọi phương thức xóa của Adapter khi người dùng chọn xóa
                            deleteFruit(position);
                            Toast.makeText(context, "Xóa", Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();

                alertDialog.show();
            }
            //show

        });

        holder.ivUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                fruitModel fruit = listFruit.get(position);
                if (position != RecyclerView.NO_POSITION) {
                    // Gọi phương thức xóa của Adapter khi người dùng chọn xóa
                    showUpdateDialog(fruit);
                    Toast.makeText(context, "Cập nhật", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFruit.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtQuantity, txtOrigin;
        ImageView ivImage, ivDel, ivUpd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtOrigin = itemView.findViewById(R.id.txtOrigin);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivDel = itemView.findViewById(R.id.ivDel);
            ivUpd = itemView.findViewById(R.id.ivUpd);
        }
    }

    //hàm xóa
    public void deleteFruit(int position) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIservice.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.d("Delete Fruit", "Deleting fruit at position: " + position);
        APIservice apiService = retrofit.create(APIservice.class);
        // Gọi phương thức Retrofit để thực hiện yêu cầu DELETE tới máy chủ
        fruitModel fruitToDelete = listFruit.get(position);
        Call<Void> call = apiService.deleteFruit(fruitToDelete.get_id());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Xóa sinh viên khỏi danh sách và cập nhật RecyclerView
                    listFruit.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                } else {
                    // Xử lý khi nhận được phản hồi lỗi từ máy chủ
                    Log.e("Delete Fruit", "Failed to delete fruit: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý khi gặp lỗi trong quá trình gửi yêu cầu
                Log.e("Delete Fruit", "Error deleting fruit: " + t.getMessage());
            }
        });
    }

    // Phương thức để hiển thị dialog cập nhật
    private void showUpdateDialog(fruitModel fruit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Inflate layout cho dialog
        LayoutInflater Inflater = LayoutInflater.from(this.context);
        View view = Inflater.from(this.context).inflate(R.layout.dialog_update, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        // Khởi tạo EditText và gán giá trị hiện tại của sản phẩm vào
        EditText editTextName = view.findViewById(R.id.edtName_up);
        EditText editTextPrice = view.findViewById(R.id.edtPrice_up);
        EditText editTextOrigin = view.findViewById(R.id.edtOrigin_up);
        EditText editTextQuantity = view.findViewById(R.id.edtQuantity_up);
        Button btnUpdate = view.findViewById(R.id.btnUpdate);
        Button btnCancel = view.findViewById(R.id.btnCancel_up);

        editTextName.setText(fruit.getName());
        editTextPrice.setText(String.valueOf(fruit.getPrice()));
        editTextOrigin.setText(fruit.getOrigin());
        editTextQuantity.setText(String.valueOf(fruit.getQuantity()));

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        // Xử lý sự kiện khi nhấn nút cập nhật trong dialog
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = editTextName.getText().toString();
                double newPrice = Double.parseDouble(editTextPrice.getText().toString());
                String newOrigin = editTextOrigin.getText().toString();
                int newQuantity = Integer.parseInt(editTextQuantity.getText().toString());
                String img = "https://i.pinimg.com/236x/2e/4d/db/2e4ddb3c2ca38d6116abba5b663bac6e.jpg";

                // Tạo đối tượng được cập nhật
                fruitModel updatedFruit = new fruitModel(newName, newPrice, newOrigin, img, newQuantity);

                // Gọi phương thức Retrofit để cập nhật
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(APIservice.DOMAIN)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                APIservice apiService = retrofit.create(APIservice.class);

                Call<fruitModel> call = apiService.updateFruit(fruit.get_id(), updatedFruit);
                call.enqueue(new Callback<fruitModel>() {
                    @Override
                    public void onResponse(Call<fruitModel> call, Response<fruitModel> response) {
                        if (response.isSuccessful()) {
                            // Cập nhật thành công
                            Call<List<fruitModel>> call1 = apiService.getfruitList();
                            call1.enqueue(new Callback<List<fruitModel>>() {
                                @Override
                                public void onResponse(Call<List<fruitModel>> call1, Response<List<fruitModel>> response) {
                                    if (response.isSuccessful()) {
                                        List<fruitModel> fetchedFruits = response.body();
                                        if (fetchedFruits != null) {
                                            listFruit.clear(); // Xóa dữ liệu cũ
                                            listFruit.addAll(fetchedFruits); // Thêm dữ liệu mới
                                            notifyDataSetChanged(); // Cập nhật RecyclerView
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
                            Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                            dialog.dismiss();
                        } else {
                            // Cập nhật thất báo
                            Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<fruitModel> call, Throwable t) {
                        // Xử lý khi gữp lỗi trong quá trình gửi yêu cầu
                        Log.e("Update Fruit", "Error updating fruit: " + t.getMessage());
                    }
                });
            }
        });

        // Hiển thị dialog
        dialog.show();
    }

}
