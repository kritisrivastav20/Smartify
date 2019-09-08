package com.smartyfy.library;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartyfy.R;
import com.smartyfy.bean_class.Client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ViewHolder> {
    private List<Client> clientList;
    private onClickListener onClickListener;

    public ClientAdapter() {
        clientList = new ArrayList<Client>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.tv_client.setText(clientList.get(i).name);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onClickListener)
                    onClickListener.onClick(viewHolder.getAdapterPosition(), clientList.get(viewHolder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }

    private void setClientList(List<Client> clientList) {
        this.clientList = clientList;
        notifyDataSetChanged();
    }

    public void setClients(JSONArray clients) {
        List<Client> clientList = new ArrayList<Client>();
        for (int i = 0, size = clients.length(); i < size ; i++) try {
            Client client = new Client();
            client.id = clients.getJSONObject(i).getString("client_id");
            client.name = clients.getJSONObject(i).getString("client_name");
            client.inactive = clients.getJSONObject(i).getInt("client_inactive");
            client.date = clients.getJSONObject(i).getString("client_date");
            client.client_ip = clients.getJSONObject(i).getString("client_ip");
            client.port = clients.getJSONObject(i).getString("client_port");
            client.wifi_username = clients.getJSONObject(i).getString("wifi_username");
            client.wifi_password = clients.getJSONObject(i).getString("wifi_password");
            client.gprs_mob = clients.getJSONObject(i).getString("gprs_no");
            clientList.add(client);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setClientList(clientList);
    }

    public void setOnClickListener(ClientAdapter.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public List<Client> getClientList() {
        return clientList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tv_client;
        ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.content_client_list, parent, false));
            tv_client = itemView.findViewById(R.id.tv_client);
        }
    }

    public interface onClickListener {
        public void onClick(int position, Client client);
    }
}
