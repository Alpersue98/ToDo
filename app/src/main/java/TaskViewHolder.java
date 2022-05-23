import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ToDo.databinding.ActivityTaskListBinding;


public class TaskViewHolder extends RecyclerView.ViewHolder {
    public final TextView nameTextView;
    private ActivityTaskListBinding binding;
    public ItemViewHolder(ItemBinding binding) {
        super(binding.getRoot());
        nameTextView = binding.nameTextView;
    }
}
