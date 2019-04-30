package pin.com.libraryseatmanagementsystem.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public abstract class MyRecyclerViewAdapter<T> extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private int layoutId;
    private List<T> data;
    /*
    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }
    public OnItemClickListener onItemClickListener;
    */
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> views;
        private View view;

        public MyViewHolder(Context _context, View _view, ViewGroup _viewGroup){
            super(_view);
            view = _view;
            views = new SparseArray<View>();
        }
        public static MyViewHolder get(Context _context, ViewGroup _viewGroup, int _layoutId) {
            View _view = LayoutInflater.from(_context).inflate(_layoutId, _viewGroup, false);
            MyViewHolder holder = new MyViewHolder(_context, _view, _viewGroup);
            return holder;
        }
        public <T extends View> T getView(int _viewId) {
            View _view = views.get(_viewId);
            if (_view == null) {
                // 创建view
                _view = view.findViewById(_viewId);
                // 将view存入views
                views.put(_viewId, _view);
            }
            return (T)_view;
        }
    }


    /*
    public void setOnItemClickListener(OnItemClickListener _onItemClickListener) {
        this.onItemClickListener = _onItemClickListener;
    }
    */
    public abstract void convert(MyViewHolder holder, T t);
    public MyRecyclerViewAdapter(Context _context, int _layoutId, List<T> _data) {
        context = _context;
        layoutId = _layoutId;
        data = _data;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = MyViewHolder.get(context, parent, layoutId);
        return holder;
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        convert(holder, data.get(position)); // convert函数需要重写，下面会讲
        /*
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onItemClickListener.onLongClick(holder.getAdapterPosition());
                    return true;
                }
            });
        }
        */
    }


}
