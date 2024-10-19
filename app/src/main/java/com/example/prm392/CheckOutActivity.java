package com.example.prm392;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CheckOutActivity extends AppCompatActivity {
    private TextView tvTotalAmount;
    private RadioGroup rgPaymentMethods;
    private EditText etCardNumber, etCardholderName, etExpirationDate, etAddress;
    private Button btnConfirmPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        rgPaymentMethods = findViewById(R.id.rgPaymentMethods);
        etCardNumber = findViewById(R.id.etCardNumber);
        etCardholderName = findViewById(R.id.etCardholderName);
        etExpirationDate = findViewById(R.id.etExpirationDate);
        etAddress = findViewById(R.id.etAddress);
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);

        // Set total amount (this should be passed from the previous activity)
        tvTotalAmount.setText("Total Amount: $" + getIntent().getDoubleExtra("TOTAL_AMOUNT", 0.00));

        rgPaymentMethods.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbDirect) {
                    etCardNumber.setVisibility(View.GONE);
                    etCardholderName.setVisibility(View.GONE);
                    etExpirationDate.setVisibility(View.GONE);
                    etAddress.setVisibility(View.VISIBLE);
                } else {
                    etCardNumber.setVisibility(View.VISIBLE);
                    etCardholderName.setVisibility(View.VISIBLE);
                    etExpirationDate.setVisibility(View.VISIBLE);
                    etAddress.setVisibility(View.GONE);
                }
            }
        });

        btnConfirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });
    }

    private void processPayment() {
        int selectedPaymentMethodId = rgPaymentMethods.getCheckedRadioButtonId();
        if (selectedPaymentMethodId == -1) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedPaymentMethodId == R.id.rbCreditCard) {
            String cardNumber = etCardNumber.getText().toString();
            String cardholderName = etCardholderName.getText().toString();
            String expirationDate = etExpirationDate.getText().toString();

            if (cardNumber.isEmpty() || cardholderName.isEmpty() || expirationDate.isEmpty()) {
                Toast.makeText(this, "Please fill in all payment information", Toast.LENGTH_SHORT).show();
                return;
            }
        } else if (selectedPaymentMethodId == R.id.rbDirect) {
            String address = etAddress.getText().toString();
            if (address.isEmpty()) {
                Toast.makeText(this, "Please fill in the delivery address", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Process the payment (this is a placeholder, actual implementation will vary)
        boolean paymentSuccess = true; // Assume payment is successful

        if (paymentSuccess) {
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
            // Send confirmation email (this is a placeholder, actual implementation will vary)
            // Navigate to order confirmation screen or main page
        } else {
            Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
        }
    }
}