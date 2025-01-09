// UploadQuestionAdapter.java
package com.example.myexamapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myexamapp.Models.UploadQuestionModel;
import com.example.myexamapp.R;
import java.util.ArrayList;

public class UploadQuestionAdapter extends RecyclerView.Adapter<UploadQuestionAdapter.UploadQuestionViewHolder> {

    private ArrayList<UploadQuestionModel> questionList;
    private Context context;

    // Constructor
    public UploadQuestionAdapter(ArrayList<UploadQuestionModel> questionList, Context context) {
        this.questionList = questionList;
        this.context = context;
    }

    @NonNull
    @Override
    public UploadQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_upload_question, parent, false);
        return new UploadQuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UploadQuestionViewHolder holder, int position) {
        UploadQuestionModel question = questionList.get(position);
        holder.questionTextView.setText(question.getQuestionText());
        holder.optionATextView.setText(question.getOptionA());
        holder.optionBTextView.setText(question.getOptionB());
        holder.optionCTextView.setText(question.getOptionC());
        holder.optionDTextView.setText(question.getOptionD());
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class UploadQuestionViewHolder extends RecyclerView.ViewHolder {

        TextView questionTextView, optionATextView, optionBTextView, optionCTextView, optionDTextView;

        public UploadQuestionViewHolder(View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.questionTextView);
            optionATextView = itemView.findViewById(R.id.optionATextView);
            optionBTextView = itemView.findViewById(R.id.optionBTextView);
            optionCTextView = itemView.findViewById(R.id.optionCTextView);
            optionDTextView = itemView.findViewById(R.id.optionDTextView);
        }
    }
}
