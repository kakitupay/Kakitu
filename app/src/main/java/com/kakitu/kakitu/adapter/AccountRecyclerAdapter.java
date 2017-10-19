package com.kakitu.kakitu.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kakitu.kakitu.R;
import com.kakitu.kakitu.activity.DecoderActivity;
import com.kakitu.kakitu.helper.SessionManager;
import com.kakitu.kakitu.model.Account;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android-dev on 10/16/17.
 */

public class AccountRecyclerAdapter extends RecyclerView.Adapter<AccountRecyclerAdapter.ViewHolder> {

    private List<Account> accounts;

    public AccountRecyclerAdapter(List<Account> accounts) {
        this.accounts = accounts;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_list_item, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Account account = accounts.get(position);
        holder.titleText.setText(account.getName());
        holder.thumbnailImage.setImageResource(account.getThumbnail());

        // Click listener
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                SessionManager sessionManager = new SessionManager(context);
                if (account.getName().equals("Pay via KakituPOS")) {
                    context.startActivity(new Intent(context, DecoderActivity.class));
                } else if (account.getName().equals("Balance")) {

                } else if (account.getName().equals("Phone Numbers")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater layoutInflater = LayoutInflater.from(context);
                    View view = layoutInflater.inflate(R.layout.phone_number_view, null);
                    builder.setView(view);

                    final TextView phone =  view.findViewById(R.id.phone_view);
                    phone.setText(sessionManager.getPhone());
                    builder.setTitle("Your Phone Number");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();


                } else if (account.getName().equals("Loans")) {

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View view;
        @BindView(R.id.title)
        TextView titleText;
        @BindView(R.id.thumbnail)
        ImageView thumbnailImage;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
