package lab.poly.demoasm_and103.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import lab.poly.demoasm_and103.IClick;
import lab.poly.demoasm_and103.R;
import lab.poly.demoasm_and103.models.Fruit;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {
    private Context context;
    private List<Fruit> listFruit;
    private IClick iClick;

    public FruitAdapter(Context context, List<Fruit> listFlower, IClick iClick) {
        this.context = context;
        this.listFruit = listFlower;
        this.iClick = iClick;
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
        holder.tvName.setText("Tên: " + listFruit.get(position).getName());
        holder.tvPrice.setText("Giá: " + listFruit.get(position).getPrice());
        holder.tvOrigin.setText("Xuất xứ: " + listFruit.get(position).getOrigin());
        holder.tvQuantity.setText("Số lượng: " + listFruit.get(position).getQuantity());
        Glide.with(context).load(listFruit.get(position).getImage()).into(holder.ivImage);
        holder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                iClick.onDeleteClick(position);
            }

        });
        holder.ivUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                iClick.onEditClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFruit.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvQuantity, tvOrigin;
        ImageView ivDel, ivUpd, ivImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.txtName);
            tvPrice = itemView.findViewById(R.id.txtPrice);
            tvOrigin = itemView.findViewById(R.id.txtOrigin);
            tvQuantity = itemView.findViewById(R.id.txtQuantity);
            ivDel = itemView.findViewById(R.id.ivDel);
            ivUpd = itemView.findViewById(R.id.ivUpd);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }

}
