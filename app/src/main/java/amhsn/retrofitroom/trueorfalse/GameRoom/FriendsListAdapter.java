package amhsn.retrofitroom.trueorfalse.GameRoom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import amhsn.retrofitroom.trueorfalse.R;
import amhsn.retrofitroom.trueorfalse.model.User;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.FriendsViewHolder> {

    private List<User> list = new ArrayList<>();
    private Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position,View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public FriendsListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FriendsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item, parent, false),mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsViewHolder holder, final int position) {
        if (list != null) {
            holder.item_name.setText(list.get(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void setList(List<User> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Nullable
    public String getUidItem(int position) {
        return list.get(position).getUser_id();
    }

    public class FriendsViewHolder extends RecyclerView.ViewHolder {
        TextView item_name, item_request;

        public FriendsViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            item_name = itemView.findViewById(R.id.item_name);
            item_request = itemView.findViewById(R.id.item_request);

            item_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position,item_request);
                        }
                    }
                }
            });
        }
    }


}
