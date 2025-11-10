import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.keder.flo.R
import com.keder.flo.SaveSongRVAdapter
import com.keder.flo.Song
import com.keder.flo.databinding.FragmentSaveSongBinding

class SaveSongFragment : Fragment() {
    lateinit var binding : FragmentSaveSongBinding
//    lateinit var songDB : SongDatabase

    private val database = Firebase.database.reference
    private lateinit var songRVAdapter: SaveSongRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSaveSongBinding.inflate(inflater, container, false)
        //songDB = SongDatabase.getInstance(requireContext())!!

        binding.lockerSavedSongRv.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL, false)

        songRVAdapter = SaveSongRVAdapter(true)
        binding.lockerSavedSongRv.adapter = songRVAdapter

        //songRVAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList<Song>)

//        songRVAdapter.setMyItemClickListener(object : SaveSongRVAdapter.MyItemClickListener{
//            override fun onRemoveSong(songId: Int) {
//                songDB.songDao().updateIsLikeById(false, songId)
//            }
//        })

        songRVAdapter.setMyItemClickListener(object : SaveSongRVAdapter.MyItemClickListener{
            override fun onRemoveSong(songId: String) {
                database.child("songs").child(songId).child("isLike").setValue(false)
                    .addOnSuccessListener {
                        Log.d("Firebase", "$songId 'like' removed")
                        loadLikedSongsFromFirebase()
                    }
            }
        })

        return binding.root
}

override fun onResume() {
    super.onResume()
    loadLikedSongsFromFirebase()
}

private fun loadLikedSongsFromFirebase() {
    database.child("songs").get().addOnSuccessListener { dataSnapshot ->
        val likedSongsList = ArrayList<Song>() // 임시 리스트

        for (snapshot in dataSnapshot.children) {
            val song = snapshot.getValue(Song::class.java)

            // 12. 'isLike'가 true인 곡만 리스트에 추가
            if (song != null && song.isLike) {
                likedSongsList.add(song)
            }
        }

        songRVAdapter.addSongs(likedSongsList)
        Log.d("Firebase", "Loaded ${likedSongsList.size} liked songs.")

    }.addOnFailureListener {
        Log.e("Firebase", "Failed to load liked songs", it)
    }
}
}