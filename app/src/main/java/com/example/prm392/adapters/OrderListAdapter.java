package com.example.prm392.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm392.Data.AppDatabase;
import com.example.prm392.R;
import com.example.prm392.activities.OrderDetailActivity;
import com.example.prm392.entity.Color;
import com.example.prm392.entity.Order;
import com.example.prm392.entity.OrderDetail;
import com.example.prm392.entity.Product;
import com.example.prm392.entity.Size;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderListAdapter extends ArrayAdapter<Order> {

    private AppDatabase appDatabase;
    private Context context;
    private List<Order> orders;

    public OrderListAdapter(Context context, List<Order> orders, AppDatabase appDatabase) {
        super(context, 0, orders);
        this.appDatabase = appDatabase;
        this.context = context;
        this.orders = orders;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        int roleId = sharedPreferences.getInt("ROLE_ID", -1);
        int accountId = sharedPreferences.getInt("ACCOUNT_ID", -1);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.viewholder_order_item, parent, false);
        }

        Order order = getItem(position);

        if (roleId == 2 && order.getAccountId() != accountId) {
            return new View(context);
        }

        TextView name = convertView.findViewById(R.id.name);
        TextView brand = convertView.findViewById(R.id.brand);
        TextView color = convertView.findViewById(R.id.color);
        TextView size = convertView.findViewById(R.id.size);
        TextView orderStatus = convertView.findViewById(R.id.order_status);
        TextView orderDate = convertView.findViewById(R.id.order_date);
        TextView amount = convertView.findViewById(R.id.amount);
        TextView totalAmount = convertView.findViewById(R.id.total_amount);
        TextView unitPrice = convertView.findViewById(R.id.unit_price);
        TextView totalPrice = convertView.findViewById(R.id.total_price);
        ImageView productImg = convertView.findViewById(R.id.product_img);
        Button detailButton = convertView.findViewById(R.id.order_detail_btn);

        appDatabase.orderDetailDao().getOrderDetailByOrderId(order.getId())
                .observeForever(orderDetails -> {
                    if (orderDetails != null && !orderDetails.isEmpty()) {
                        double total = 0;
                        int totalProduct = 0;

                        for (OrderDetail detail : orderDetails) {
                            total += detail.getUnitPrice() * detail.getQuantity();
                            totalProduct += detail.getQuantity();
                        }

                        setProductDetail(orderDetails, name, productImg, brand, color, size);

                        amount.setText("x" + orderDetails.get(0).getQuantity());
                        unitPrice.setText("đ" + orderDetails.get(0).getUnitPrice());
                        totalAmount.setText(totalProduct + " products");
                        totalPrice.setText("Total: đ" + total);
                    }
                });


        orderStatus.setText("Status: " + order.getStatus());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDate = dateFormat.format(order.getOrderDate());
        orderDate.setText("Order Date: " + formattedDate);

        detailButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("orderId", order.getId());
            context.startActivity(intent);
        });

        return convertView;
    }

    public void setOrders(List<Order> orders) {
        this.orders.clear();
        this.orders.addAll(orders);
    }

    public void setProductDetail(List<OrderDetail> orderDetails, TextView name, ImageView productImg, TextView brand, TextView color, TextView size) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            long productId = orderDetails.get(0).getProductId();
            Product product = appDatabase.productDao().getProductById((int) productId);
            long sizeID = orderDetails.get(0).getSizeId();
            long colorId = orderDetails.get(0).getColorId();
            long brandId = product.getBrandId();
            String image = appDatabase.imageShoeDao().getFirstImageByProductId((int) productId);
            String brandName = appDatabase.brandDao().getBrandNameById(brandId);
            Size shoeSize = appDatabase.sizeDao().getSizeBySizeId((int) sizeID);
            Color shoeColor = appDatabase.colorDao().getColorById((int) colorId);


            ((Activity) context).runOnUiThread(() -> {
                if (product != null) {
                    name.setText(product.getProductName());
                    productImg.setImageBitmap(BitmapFactory.decodeFile(image));
                    brand.setText(brandName);
                    color.setText(shoeColor.getColor());
                    size.setText(String.valueOf(shoeSize.getSize()));

                } else {
                    Toast.makeText(context, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}