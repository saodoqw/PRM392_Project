package com.example.prm392.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.example.prm392.entity.Order;
import com.example.prm392.entity.OrderDetail;
import com.example.prm392.entity.Product;

import java.util.List;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.viewholder_order_item, parent, false);
        }

        Order order = getItem(position);

        // Bind dữ liệu với các View
        TextView name = convertView.findViewById(R.id.name);
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

//                        setProductDetail(orderDetails, name);

                        amount.setText("x" + orderDetails.get(0).getQuantity());
                        unitPrice.setText("đ" + orderDetails.get(0).getUnitPrice());
                        totalAmount.setText(totalProduct + " products");
                        totalPrice.setText("Total: đ" + total);
                    }
                });


        orderStatus.setText("Status: " + order.getStatus());
        orderDate.setText("Order Date: " + order.getOrderDate());
        productImg.setImageResource(R.drawable.ic_launcher_background);

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

//    public void setProductDetail(List<OrderDetail> orderDetails, TextView name) {
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//
//        executor.execute(() -> {
//            long productId = orderDetails.get(0).getProductId();
//            Product product = appDatabase.productDao().getProductById(productId);
//
//            ((Activity) context).runOnUiThread(() -> {
//                if (product != null) {
//                    name.setText(product.getProductName());
////                                    Glide.with(this).load(product.getImageUrl()).into(productImg);
//                } else {
//                    Toast.makeText(context, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
//                }
//            });
//        });
//    }
}