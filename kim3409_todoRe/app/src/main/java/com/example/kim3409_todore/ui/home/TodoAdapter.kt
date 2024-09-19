package com.example.kim3409_todore.ui.home

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kim3409_todore.R
import com.example.kim3409_todore.data.todo.TodoEntity

class TodoAdapter(
    private var todoList: List<TodoEntity>,
    private val onItemClick:(Long) -> Unit,
    private val onDeleteClick: (Long) -> Unit,
    private val onCompleteClick: (Long) -> Unit,
    private val onEditClick: (Long) -> Unit
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val todoItem: LinearLayout = itemView.findViewById(R.id.todoItem)
        val TodoTitle: TextView = itemView.findViewById(R.id.todoTitle)
        val btnDelete: Button = itemView.findViewById(R.id.deleteBtn)
        val btnComplete: Button = itemView.findViewById(R.id.completeBtn)
        val btnEdit: Button = itemView.findViewById(R.id.editBtn)
    }

    override fun getItemCount() = todoList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return TodoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todoItem = todoList[position]
        holder.TodoTitle.text = todoItem.title

        if (todoItem.isCompleted) { // 생성된 뷰에 데이터를 바인딩하는 작업을 여기서 진행해야함
            holder.TodoTitle.paintFlags = holder.TodoTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.TodoTitle.paintFlags = holder.TodoTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
        holder.todoItem.setOnClickListener {
            onItemClick(todoItem.id)
        }

        holder.btnDelete.setOnClickListener {
            onDeleteClick(todoItem.id)
        }
        holder.btnComplete.setOnClickListener {
            onCompleteClick(todoItem.id)
        }
        holder.btnEdit.setOnClickListener {
            onEditClick(todoItem.id)
        }
    }

//    @SuppressLint("NotifyDataSetChanged")
//    fun updateTodoList(newTodoList: List<TodoEntity>) {
//        todoList = newTodoList
//        notifyDataSetChanged()
//    }
}
