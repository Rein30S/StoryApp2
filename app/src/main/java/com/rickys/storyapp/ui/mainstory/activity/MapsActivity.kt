package com.rickys.storyapp.ui.mainstory.activityimport android.Manifestimport android.content.pm.PackageManagerimport android.content.res.Resourcesimport android.os.Buildimport android.os.Bundleimport android.view.WindowInsetsimport android.view.WindowManagerimport android.widget.Toastimport androidx.activity.result.contract.ActivityResultContractsimport androidx.activity.viewModelsimport androidx.appcompat.app.AppCompatActivityimport androidx.core.content.ContextCompatimport com.google.android.gms.maps.CameraUpdateFactoryimport com.google.android.gms.maps.GoogleMapimport com.google.android.gms.maps.OnMapReadyCallbackimport com.google.android.gms.maps.SupportMapFragmentimport com.google.android.gms.maps.model.LatLngimport com.google.android.gms.maps.model.MapStyleOptionsimport com.google.android.gms.maps.model.MarkerOptionsimport com.rickys.storyapp.Rimport com.rickys.storyapp.databinding.ActivityMapsBindingimport com.rickys.storyapp.ui.mainstory.viewmodel.ViewModelFactoryimport com.rickys.storyapp.api.response.ListStoryItemimport com.rickys.storyapp.ui.mainstory.viewmodel.MapsViewModelimport timber.log.Timberclass MapsActivity : AppCompatActivity(), OnMapReadyCallback {    private lateinit var mMap: GoogleMap    private lateinit var binding: ActivityMapsBinding    private val viewModel: MapsViewModel by viewModels {        ViewModelFactory(this)    }    override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        binding = ActivityMapsBinding.inflate(layoutInflater)        setContentView(binding.root)        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment        mapFragment.getMapAsync(this)        hideSystemUI()    }    override fun onMapReady(googleMap: GoogleMap) {        mMap = googleMap        mMap.uiSettings.isZoomControlsEnabled = true        mMap.uiSettings.isIndoorLevelPickerEnabled = true        mMap.uiSettings.isCompassEnabled = true        mMap.uiSettings.isMapToolbarEnabled = true        getMyLocation()        setUpViewModel()        setMapStyle()    }    private val requestPermissionLauncher =        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->            if (isGranted) {                getMyLocation()            } else {                Toast.makeText(                    this, "No Permission!", Toast.LENGTH_SHORT                ).show()            }        }    private fun setUpViewModel() {        viewModel.listLocationStory.observe(this) {            showStoryMarker(it)        }    }    private fun showStoryMarker(storyItems: List<ListStoryItem>) {        storyItems.forEach { story ->            if (story.lat != null && story.lon != null) {                val location = LatLng(story.lat, story.lon)                mMap.addMarker(                    MarkerOptions()                        .title(story.name)                        .position(location)                        .snippet("${story.name}: ${story.description}")                )                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 7f))            }        }    }    private fun getMyLocation() {        if (ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED        ) {            mMap.isMyLocationEnabled = true        } else {            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)        }    }    private fun setMapStyle() {        try {            val success =                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))            if (!success) {                Timber.e("Style parsing failed.")            }        } catch (exception: Resources.NotFoundException) {            Timber.e("Can't find style. Error: ", exception)        }    }    @Suppress("DEPRECATION")    private fun hideSystemUI() {        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {            window.insetsController?.hide(WindowInsets.Type.statusBars())        } else {            window.setFlags(                WindowManager.LayoutParams.FLAG_FULLSCREEN,                WindowManager.LayoutParams.FLAG_FULLSCREEN            )        }        supportActionBar?.hide()    }}