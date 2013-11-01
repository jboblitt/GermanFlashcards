package com.jbslade.flashcards.language.german.ui;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.app.Fragment;
import android.support.v7.appcompat.R;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by Justin on 9/9/13.
 */
public class FragmentFlashCard extends Fragment
{
    /**
     * The fragment argument representing the section text for this
     * fragment.
     */
    public static final String ARG_FLASH_CARD_ID = "card_id";
//    private Animation.AnimationListener m_animationListener;

    public FragmentFlashCard()
    {
    }

//    @Override
//    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim)
//    {
//        if (enter && m_animationListener != null)
//        {
//            Animator animator = new Animator()
//            {
//                @Override
//                public long getStartDelay()
//                {
//                    return 0;
//                }
//
//                @Override
//                public void setStartDelay(long startDelay)
//                {
//
//                }
//
//                @Override
//                public Animator setDuration(long duration)
//                {
//                    return null;
//                }
//
//                @Override
//                public long getDuration()
//                {
//                    return 0;
//                }
//
//                @Override
//                public void setInterpolator(TimeInterpolator value)
//                {
//
//                }
//
//                @Override
//                public boolean isRunning()
//                {
//                    return false;
//                }
//            };
//            animator.addListener(new Animator.AnimatorListener()
//            {
//                @Override
//                public void onAnimationStart(Animator animation)
//                {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animation)
//                {
//                    m_animationListener.onAnimationEnd();
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animation)
//                {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animation)
//                {
//
//                }
//            });
//            return animator;
//        }
//        else
//            return super.onCreateAnimator(transit, enter, nextAnim);
//    }

//    public void setAnimationListener(Animation.AnimationListener animationListener)
//    {
//        m_animationListener = animationListener;
//    }

}
