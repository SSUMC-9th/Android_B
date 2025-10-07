import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.keder.flo.R
import com.keder.flo.SaveSongRVAdapter
import com.keder.flo.Song
import com.keder.flo.databinding.FragmentSaveSongBinding

class SaveSongFragment : Fragment() {
    lateinit var binding : FragmentSaveSongBinding
    private var songData = ArrayList<Song>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSaveSongBinding.inflate(inflater, container, false)

        songData.apply{
            add(Song("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
            add(Song("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
        }

        val songRVAdapter = SaveSongRVAdapter(songData, true)
        binding.lockerSavedSongRv.adapter = songRVAdapter
        binding.lockerSavedSongRv.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL, false)
        songRVAdapter.setMyItemClickListener(object : SaveSongRVAdapter.MyItemClickListener{
            override fun onRemoveSong(songId: Int) {

            }
        })


        return binding.root
    }
}