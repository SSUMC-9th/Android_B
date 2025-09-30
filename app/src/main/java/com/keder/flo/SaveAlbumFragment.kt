import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.keder.flo.databinding.FragmentSaveAlbumBinding

class SaveAlbumFragment : Fragment() {
    lateinit var binding : FragmentSaveAlbumBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSaveAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }
}