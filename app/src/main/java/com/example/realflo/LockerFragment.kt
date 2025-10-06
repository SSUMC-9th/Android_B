package com.example.realflo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.realflo.databinding.FragmentLockerBinding

class LockerFragment : Fragment() {

    private lateinit var binding: FragmentLockerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val lockerData = ArrayList<LockerData>()
        lockerData.apply {
            add(LockerData(R.drawable.img_album_exp2, "친구1", "friend1@email.com"))
            add(LockerData(R.drawable.img_album_exp2, "친구2", "friend2@email.com"))
            add(LockerData(R.drawable.img_album_exp2, "친구3", "friend3@email.com"))
            add(LockerData(R.drawable.img_album_exp2, "친구4", "friend4@email.com"))
        }

        val lockerAdapter = LockerAdapter(lockerData) { /* 클릭 리스너 */ }
        binding.friendRecyclerview.adapter = lockerAdapter
        binding.friendRecyclerview.layoutManager = LinearLayoutManager(context)
    }
}
