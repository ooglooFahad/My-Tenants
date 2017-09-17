package ooglootenants.andorid.com.tenancyapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    public List<Tenant_class> MyTenant_list = new ArrayList<>();
    Context mycontext;

    public RecyclerViewAdapter(List<Tenant_class> MyTenant_list, Context context) {
        this.MyTenant_list = MyTenant_list;
        this.mycontext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_tenant, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(itemView, mycontext, MyTenant_list);

        return myViewHolder;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mytenantname, mytenantphone, mytenantrent, mytenantemail;
        List<Tenant_class> Tenantlist = new ArrayList<>();
        Context mycontext;

        public MyViewHolder(View itemView, Context context, List<Tenant_class> tenantlist) {

            super(itemView);
            this.Tenantlist = tenantlist;
            this.mycontext = context;
            itemView.setOnClickListener(this);
            mytenantname = (TextView) itemView.findViewById(R.id.Recycler_Tenant_Name);
            mytenantphone = (TextView) itemView.findViewById(R.id.Recycler_Tenant_Phone);
            mytenantrent = (TextView) itemView.findViewById(R.id.Recycler_Tenant_Rent);
            mytenantemail = (TextView) itemView.findViewById(R.id.Recycler_Tenant_Email);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Tenant_class tenant_class = this.Tenantlist.get(position);
            Intent intent = new Intent(this.mycontext, Tenant_Details.class);
            intent.putExtra("rid", tenant_class.getR_id());
            intent.putExtra("lid", tenant_class.getL_id());
            intent.putExtra("name", tenant_class.getRenter_Name());
            intent.putExtra("phone", tenant_class.getRenter_Phoneno());
            intent.putExtra("email", tenant_class.getRenter_Email());
            intent.putExtra("cnic", tenant_class.getRenter_CNIC());
            intent.putExtra("rent", tenant_class.getRent_Amount());
            intent.putExtra("security", tenant_class.getRent_Security());
            intent.putExtra("paymentdate", tenant_class.getRenter_Paymentdate());
            intent.putExtra("paymentreceived", tenant_class.getPayment_Received());
            intent.putExtra("notification", tenant_class.getNotifications());
            intent.putExtra("rentnotificationdate", tenant_class.getRentNotificationDate());
            this.mycontext.startActivity(intent);

        }
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.MyViewHolder holder, int position) {

        Tenant_class MyList = MyTenant_list.get(position);
        holder.mytenantname.setText(MyList.getRenter_Name());
        holder.mytenantphone.setText(MyList.getRenter_Phoneno());
        holder.mytenantrent.setText(MyList.getRent_Amount());
        holder.mytenantemail.setText(MyList.getRenter_Email());
    }

    @Override
    public int getItemCount() {
        return MyTenant_list.size();
    }
}
