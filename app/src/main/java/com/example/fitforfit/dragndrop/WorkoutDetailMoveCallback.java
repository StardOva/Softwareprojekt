package com.example.fitforfit.dragndrop;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforfit.adapter.WorkoutDetailAdapter;

public class WorkoutDetailMoveCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperContract mAdapter;

    public WorkoutDetailMoveCallback(ItemTouchHelperContract mAdapter) {
        this.mAdapter = mAdapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;

        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        mAdapter.onRowMoved(viewHolder.getAbsoluteAdapterPosition(), target.getAbsoluteAdapterPosition());

        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof WorkoutDetailAdapter.WorkoutDetailViewHolder) {
                WorkoutDetailAdapter.WorkoutDetailViewHolder myViewHolder = (WorkoutDetailAdapter.WorkoutDetailViewHolder) viewHolder;
                mAdapter.onRowSelected(myViewHolder);
            }
        }

        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (viewHolder instanceof WorkoutDetailAdapter.WorkoutDetailViewHolder) {
            WorkoutDetailAdapter.WorkoutDetailViewHolder myViewHolder = (WorkoutDetailAdapter.WorkoutDetailViewHolder) viewHolder;
            mAdapter.onRowClear(myViewHolder);
        }
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    public interface ItemTouchHelperContract {
        void onRowMoved(int fromPosition, int toPosition);

        void onRowSelected(WorkoutDetailAdapter.WorkoutDetailViewHolder myViewHolder);

        void onRowClear(WorkoutDetailAdapter.WorkoutDetailViewHolder myViewHolder);
    }
}
