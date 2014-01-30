package org.jboss.demo.loanmanagement.widget;

import java.util.List;
import org.jboss.demo.loanmanagement.R;
import org.jboss.demo.loanmanagement.Util;
import org.jboss.demo.loanmanagement.model.Asset;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * A list adapter for assets.
 * 
 * @param <T> the asset class
 */
public final class AssetAdapter<T extends Asset> extends ArrayAdapter<T> {

    private final List<T> assets;

    /**
     * @param activity the activity where the list using this adapter exists (cannot be <code>null</code>)
     * @param assetCollection the asset collection (can be <code>null</code> or empty)
     */
    public AssetAdapter( final Context activity,
                         final List<T> assetCollection ) {
        super(activity, R.layout.readonly_asset, assetCollection);
        this.assets = assetCollection;
    }

    /**
     * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @SuppressWarnings( "unchecked" )
    @Override
    public View getView( final int position,
                         final View view,
                         final ViewGroup parent ) {
        View rowView = view;
        AssetHolder holder = null;

        if (rowView == null) {
            final LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
            rowView = inflater.inflate(R.layout.readonly_asset, parent, false);

            holder = new AssetHolder();
            holder.txtAmount = (TextView)rowView.findViewById(R.id.txt_asset_amount);
            holder.txtDescription = (TextView)rowView.findViewById(R.id.txt_asset_description);

            rowView.setTag(holder);
        } else {
            holder = (AssetHolder)rowView.getTag();
        }

        final Asset asset = this.assets.get(position);
        final String amount = ((asset.getAmount() <= 0) ? Util.EMPTY_STRING : Double.toString(asset.getAmount()));
        holder.txtAmount.setText(amount);
        holder.txtDescription.setText(asset.getDescription());

        return rowView;
    }

    /**
     * Refreshes the autos list content.
     */
    public void refresh() {
        clear();
        addAll(this.assets);
    }

    class AssetHolder {

        TextView txtAmount;
        TextView txtDescription;

    }

}
