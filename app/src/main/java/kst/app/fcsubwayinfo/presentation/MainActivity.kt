package kst.app.fcsubwayinfo.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import kst.app.fcsubwayinfo.R
import kst.app.fcsubwayinfo.databinding.ActivityMainBinding
import kst.app.fcsubwayinfo.extension.toGone
import kst.app.fcsubwayinfo.extension.toVisible
import kst.app.fcsubwayinfo.presentation.stationarrivals.StationArrivalsFragmentArgs

class MainActivity : AppCompatActivity() {

    //뷰 바인딩 lazy 선언
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    //네이게이션 컨트롤러 (Fragment를 사용하기 위한 새로운 방식) *마치 IOS의 뷰 컨트롤러를 사용하는 것 같음*
    //네비게이션 컨트롤러 lazy 선언
    private val navigationController by lazy {
        //Fragment를 넣기 위한 NavigationContainer를 가져와서 NavHostFragment의 navController로 선언
        (supportFragmentManager.findFragmentById(R.id.mainNavigationHostContainer) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()     // 뷰 선언
        bindViews()     // 뷰 바인드
    }

    // 앱 바에서 뒤로 가기 버튼을 클릭 했을 때 Activity 전환이 아닌 Fragment를 전환하고 싶을 때 사용용
   override fun onSupportNavigateUp(): Boolean {
        Log.d("gwan2103","onSupportNavigateUp >>>>>> ${navigationController.navigateUp() || super.onSupportNavigateUp()}")
        return navigationController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun initViews() {

        setSupportActionBar(binding.toolbar)    // 액션바에 제작한 툴바를 활동의 앱 바로 설정
        setupActionBarWithNavController(navigationController)   // 만든 네비게이션 컨트롤러를 사용
    }

    private fun bindViews() {
        //네비게이션 상태의 변환(프래그 먼트의 움직임)에 따른 동작 변환
        navigationController.addOnDestinationChangedListener { _, destination, argument ->
            if (destination.id == R.id.station_arrivals_dest) {
                title = StationArrivalsFragmentArgs.fromBundle(argument!!).station.name
                binding.toolbar.toVisible()
            } else {
                binding.toolbar.toGone()
            }
        }
    }
}