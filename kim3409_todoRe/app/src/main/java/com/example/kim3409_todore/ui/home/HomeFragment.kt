package com.example.kim3409_todore.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kim3409_todore.R
import com.example.kim3409_todore.TodoDetailActivity
import com.example.kim3409_todore.data.todo.TodoEntity
import com.example.kim3409_todore.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var homeViewModel: HomeViewModel
    private var todoList = mutableListOf<TodoEntity>()
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // ViewModel 초기화
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        // Adapter 초기화
        todoAdapter = TodoAdapter(
            todoList,
            onItemClick ={ id ->
                val intent = Intent(requireContext(),TodoDetailActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra("isEdit", false)
                startActivity(intent)
                //Activity 내에서
                //val intent = Intent(this, TodoDetailActivity::class.java)
                //
                //Fragment 내에서
                //val intent = Intent(requireContext(), TodoDetailActivity::class.java)
                Unit},
            onDeleteClick = { id ->
                homeViewModel.delete(TodoEntity(id = id, title = "", description = "", isCompleted = false))
            },
            onCompleteClick = { id ->
                val todo = todoList.find { it.id == id } ?: return@TodoAdapter
                homeViewModel.update(todo.copy(isCompleted = !todo.isCompleted))
            },
            onEditClick = { id ->
                val intent = Intent(requireContext(),TodoDetailActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra("isEdit", true)
                startActivity(intent)
            }
        )

        // RecyclerView 설정
        binding.todoListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.todoListRecyclerView.adapter = todoAdapter

        // ViewModel의 데이터를 관찰하여 RecyclerView 업데이트
        homeViewModel.getAll().observe(viewLifecycleOwner, Observer { todos ->
            todos?.let {
                todoList.clear()
                todoList.addAll(it)
                todoAdapter.notifyDataSetChanged()
            }
        })
        homeViewModel.weather.observe(viewLifecycleOwner, Observer { weather ->
            weather?.let {
                binding.weatherText.text = "현재 온도: $weather"
            }
        })
        binding.todoListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.todoListRecyclerView.adapter = todoAdapter
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_page_eidt)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}