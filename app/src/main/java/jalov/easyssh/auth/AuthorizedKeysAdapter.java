package jalov.easyssh.auth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import jalov.easyssh.R;

/**
 * Created by jalov on 2018-01-20.
 */

public class AuthorizedKeysAdapter extends BaseAdapter {
    private AuthorizedKeysManager authorizedKeysManager;
    private List<AuthorizedKey> authorizedKeys;
    private static LayoutInflater inflater;

    public AuthorizedKeysAdapter(AuthorizedKeysActivity activity, AuthorizedKeysManager authorizedKeysManager) {
        this.authorizedKeysManager = authorizedKeysManager;
        this.authorizedKeys = authorizedKeysManager.getKeys();
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return authorizedKeys.size();
    }

    @Override
    public Object getItem(int i) {
        return authorizedKeys.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    static class ViewHolder {
        TextView type;
        TextView comment;
        Button button;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null) {
            view = inflater.inflate(R.layout.authorized_key_list_item, null);
            holder = new ViewHolder();
            holder.type = view.findViewById(R.id.tv_key_type);
            holder.comment = view.findViewById(R.id.tv_key_comment);
            holder.button = view.findViewById(R.id.btn_del_key);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.type.setText(authorizedKeys.get(i).getType().toString());
        holder.comment.setText(authorizedKeys.get(i).getComment());
        holder.button.setOnClickListener(v -> {
            authorizedKeysManager.removeAuthorizedKey(i);
            this.notifyDataSetChanged();
        });

        return view;
    }
}
