package com.jbslade.flashcards.language.german.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.jbslade.flashcards.language.german.R;
import com.jbslade.flashcards.language.german.interfaces.ListBasicAdapterControllerInterface;

import java.util.ArrayList;

/**
 * Created by Justin on 10/9/13.
 */
public class ListBasicAdapter<T> extends BaseAdapter
{
    private ListBasicAdapterControllerInterface<T> m_adapterController;

    public ListBasicAdapter()
    {
    }

    public boolean addElemsToList(ArrayList<T> listElems)
    {
        boolean added = m_adapterController.addElemsToList(listElems);
        this.notifyDataSetChanged();
        return added;
    }


    @Override
    public int getCount()
    {
        return m_adapterController.getCount();
    }

    @Override
    public Object getItem(int position)
    {
        return m_adapterController.getItem(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        /*
         * A ViewHolder keeps references to children views to avoid unnecessary calls to
         * findViewById() on each row.
         */
        ViewHolder holder;

        /* When convertView is not null, we can reuse it directly, there is no need
         to reinflate it. We only inflate a new View when the convertView supplied
         by ListView is null.*/
        if (convertView == null)
        {
            holder = new ViewHolder();
            Context context = parent.getContext();
            convertView = reinflateViewHolder(context, holder);
        }
        else
        {
            // Get the ViewHolder back to get fast access to the TextView and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }

        //Populate the row fields from element Object
        String h1 = m_adapterController.getHeader1Text(position);
        String h2a = m_adapterController.getHeader2aText(position);
        String h2b = m_adapterController.getHeader2bText(position);
        if (h1 != null)
            holder.m_header1.setText(h1);
        else
            holder.m_header1.setVisibility(View.GONE);
        if (h2a != null)
            holder.m_header2a.setText(h2a);
        else
            holder.m_header2a.setVisibility(View.GONE);
        if (h2b != null)
            holder.m_header2b.setText(h2b);
        else
            holder.m_header2b.setVisibility(View.GONE);


        return convertView;
    }

    private View reinflateViewHolder(Context context, ViewHolder holder)
    {
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View reinflatedRowView = inflater.inflate(R.layout.list_base_adapter_row, null);
        // Creates a ViewHolder and store references to the two children views
        // we want to bind data to.
        holder.m_icon =
                (ImageView) reinflatedRowView.findViewById(R.id.ImageView_ListBasicRow_Icon);
        View linearLayout = reinflatedRowView.findViewById(R.id.LinearLayout_ListBasicRow);
        holder.m_header1 =
                (TextView) linearLayout.findViewById(R.id.TextView_ListBasicRow_Header1);
        holder.m_header2a =
                (TextView) linearLayout.findViewById(R.id.TextView_ListBasicRow_Header2_a);
        holder.m_header2b =
                (TextView) linearLayout.findViewById(R.id.TextView_ListBasicRow_Header2_b);

        // Set ViewHolder to tag of View to retrieve later.
        reinflatedRowView.setTag(holder);
        return reinflatedRowView;
    }

    public void setAdapterController(ListBasicAdapterControllerInterface<T> adapterController)
    {
        m_adapterController = adapterController;
    }

    /**
     * @Description Used for efficiency to hold views. A ViewHolder
     * keeps references to children views to avoid unnecessary calls
     * to findViewById() on each row.
     */
    static class ViewHolder
    {
        public ImageView m_icon;
        public TextView m_header1;
        public TextView m_header2a;
        public TextView m_header2b;
    }
}
