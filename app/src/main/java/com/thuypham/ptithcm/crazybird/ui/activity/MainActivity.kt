package com.thuypham.ptithcm.crazybird.ui.activity

import android.os.Bundle
import com.thuypham.ptithcm.crazybird.R
import com.thuypham.ptithcm.crazybird.base.BaseActivity
import com.thuypham.ptithcm.crazybird.databinding.ActivityMainBinding
import com.thuypham.ptithcm.crazybird.extension.gone
import com.thuypham.ptithcm.crazybird.extension.hideSystemBars
import com.thuypham.ptithcm.crazybird.extension.setOnSingleClickListener
import com.thuypham.ptithcm.crazybird.extension.show

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemBars()
    }

    override fun setupView() {
        updateScore(0)
        binding.apply {
            gameView.setOnScoreUpdateListener { score ->
                updateScore(score)
            }
            gameView.setIsGameOverListener { score ->
                gameOver()
            }

            btnStart.setOnSingleClickListener {
                startGame()
            }

            layoutEndGame.root.setOnSingleClickListener {
                restartGame()
            }

        }
    }

    private fun gameOver() {
        binding.apply {
            btnStart.gone()
            layoutEndGame.root.show()
        }
    }

    private fun restartGame() {
        binding.apply {
            gameView.resetGame()
            prepareToStartGame()
        }
    }

    private fun startGame() {
        binding.apply {
            prepareToStartGame()
            gameView.startGame()
        }

    }

    private fun prepareToStartGame(){
        binding.apply {
            updateScore(0)
            tvScore.show()
            layoutEndGame.root.gone()
            btnStart.gone()
        }
    }

    private fun updateScore(score: Int) {
        binding.tvScore.text = getString(R.string.score, score)
    }

}