package com.thuypham.ptithcm.crazybird.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.thuypham.ptithcm.crazybird.R
import com.thuypham.ptithcm.crazybird.extension.setOnSingleClickListener
import com.thuypham.ptithcm.crazybird.extension.show

abstract class BaseFragment<T : ViewDataBinding>(private val layoutId: Int) : Fragment() {

    lateinit var binding: T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(this.javaClass.simpleName, "onCreateView")
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(this.javaClass.simpleName, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            executePendingBindings()
        }
        setupLogic()
        setupView()
        setupDataObserver()
    }

    open fun setupLogic() {}
    abstract fun setupView()
    open fun setupDataObserver() {}

    fun showLoading() {
        (requireActivity() as BaseActivity<*>).showLoading()
    }

    fun hideLoading() {
        (requireActivity() as BaseActivity<*>).hideLoading()
    }

    fun setToolbarTitle(title: String, onClick: ((View) -> Unit?)? = null) {
        binding.root.findViewById<AppCompatTextView>(R.id.tvTitle).apply {
            show()
            text = title
            setOnSingleClickListener { onClick?.invoke(this) }
        }
    }

    fun setToolbarTitle(titleRes: Int, onClick: ((View) -> Unit?)? = null) {
        binding.root.findViewById<AppCompatTextView>(R.id.tvTitle).apply {
            show()
            text = getString(titleRes)
            setOnSingleClickListener { onClick?.invoke(this) }
        }
    }

    fun setLeftBtn(iconResID: Int, onClick: ((View) -> Unit?)? = null) {
        binding.root.findViewById<AppCompatImageView>(R.id.ivLeft).apply {
            show()
            setImageResource(iconResID)
            setOnSingleClickListener { onClick?.invoke(this) }
        }
    }

    fun setRightBtn(iconResID: Int, onClick: ((View) -> Unit?)? = null) {
        binding.root.findViewById<AppCompatImageView>(R.id.ivRight).apply {
            show()
            setImageResource(iconResID)
            setOnSingleClickListener { onClick?.invoke(this) }
        }
    }

    fun setSubRightBtn(iconResID: Int, onClick: ((View) -> Unit?)? = null) {
        binding.root.findViewById<AppCompatImageView>(R.id.ivSubRight).apply {
            show()
            setImageResource(iconResID)
            setOnSingleClickListener { onClick?.invoke(this) }
        }
    }

    fun setSubRight2Btn(iconResID: Int, onClick: ((View) -> Unit?)? = null) {
        binding.root.findViewById<AppCompatImageView>(R.id.ivSubRight2).apply {
            show()
            setImageResource(iconResID)
            setOnSingleClickListener { onClick?.invoke(this) }
        }
    }

    fun runOnUiThread(runnable: Runnable?) {
        if (activity == null || !isAdded) {
            return
        }
        activity?.runOnUiThread(runnable)
    }

    protected fun showSnackBar(resMessage: Int) {
        runOnUiThread(Runnable {
            if (view != null) {
                val snackBar = Snackbar.make(
                    view ?: return@Runnable, (activity
                        ?: return@Runnable).getString(resMessage), Snackbar.LENGTH_LONG
                )
                snackBar.show()
            }
        })
    }

    fun showSnackBar(message: String?) {
        runOnUiThread(Runnable {
            if (view != null) {
                val snackBar = Snackbar.make(
                    view ?: return@Runnable, message
                        ?: return@Runnable, Snackbar.LENGTH_SHORT
                )
                snackBar.show()
            }
        })
    }

    protected fun hideKeyboard() {
        if (activity == null) {
            return
        }
        val view = activity?.currentFocus
        if (view != null) {
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(this.javaClass.simpleName, "onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.d(this.javaClass.simpleName, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(this.javaClass.simpleName, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(this.javaClass.simpleName, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(this.javaClass.simpleName, "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(this.javaClass.simpleName, "onDestroyView")
        hideKeyboard()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(this.javaClass.simpleName, "onDestroy")
    }

}