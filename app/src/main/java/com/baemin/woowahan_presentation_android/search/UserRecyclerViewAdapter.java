package com.baemin.woowahan_presentation_android.search;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baemin.woowahan_presentation_android.model.User;

import co.moonmonkeylabs.realmsearchview.RealmSearchAdapter;
import io.realm.Realm;
import io.realm.RealmViewHolder;

/**
 * Created by leetaejun on 2016. 2. 18..
 */
public class UserRecyclerViewAdapter extends RealmSearchAdapter<User, UserRecyclerViewAdapter.ViewHolder> {

    public UserRecyclerViewAdapter(
            Context context,
            Realm realm,
            String filterColumnName) {
        super(context, realm, filterColumnName);
    }

    public class ViewHolder extends RealmViewHolder {

        private final UserItemView userItemView;

        public ViewHolder(UserItemView userItemView) {
            super(userItemView);
            this.userItemView = userItemView;
        }
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
        UserItemView userItemView = new UserItemView(viewGroup.getContext());
        ViewHolder vh = new ViewHolder(userItemView);

        return vh;
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int position) {
        final User user = realmResults.get(position);
        viewHolder.userItemView.bind(user);
    }

    @Override
    public ViewHolder convertViewHolder(RealmViewHolder viewHolder) {
        return ViewHolder.class.cast(viewHolder);
    }
}
