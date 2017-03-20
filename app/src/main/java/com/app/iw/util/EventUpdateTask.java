package com.app.iw.util;

import android.os.AsyncTask;
import android.util.Log;

import com.app.iw.callbacks.DaoOperationComplete;
import com.app.iw.model.Rule;
import com.app.iw.model.dao.DaoSession;
import com.app.iw.model.dao.PhaseDao;
import com.app.iw.model.dao.RuleDao;
import com.app.iw.model.firebase.Event;
import com.app.iw.model.firebase.Phase;
import com.google.firebase.database.DataSnapshot;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Created by zeeshan on 3/13/2017.
 */

public class EventUpdateTask extends AsyncTask<DataSnapshot, Void, com.app.iw.model.Event> {
    public static String TAG = EventUpdateTask.class.getSimpleName();
    private WeakReference<DaoSession> daoSessionWeakReference;
    private WeakReference<DaoOperationComplete> daoOperationCompleteWeakReference;

    public EventUpdateTask(DaoSession daoSession, DaoOperationComplete
            daoOperationComplete) {
        daoSessionWeakReference = new WeakReference<DaoSession>(daoSession);
        daoOperationCompleteWeakReference = new WeakReference<DaoOperationComplete>
                (daoOperationComplete);
    }


    @Override
    protected com.app.iw.model.Event doInBackground(DataSnapshot... dataSnapshots) {
        Log.d(TAG, "updating events" + dataSnapshots[0]);
        com.app.iw.model.Event daoEvent = null;

        DataSnapshot dataSnapshot =dataSnapshots[0];
            if (dataSnapshot.getValue() != null) {
                //delete all events so that any event removed while the listeners are not active also gets removed.

                PhaseDao phaseDao = daoSessionWeakReference.get().getPhaseDao();
                Event event = dataSnapshot.getValue(Event.class);
                event.setId(dataSnapshot.getKey());

                daoEvent = new com.app.iw.model.Event();
                daoEvent.setId(event.getId());
                daoEvent.setDescription(event.getDescription());
                daoEvent.setName(event.getName());
                daoEvent.setImageUrl(event.getImageUrl());
                daoEvent.setStartDate(event.getStartDate());
                daoEvent.setQuizId(event.getQuizId());

                daoSessionWeakReference.get().getEventDao().insertOrReplace(daoEvent);
                //delete phases and rules for this event skipping this will create multiple phases
                // and rules
                daoSessionWeakReference.get().getRuleDao().queryBuilder().where(RuleDao.Properties
                        .EventId.eq(daoEvent.getId())).buildDelete().executeDeleteWithoutDetachingEntities();
                daoSessionWeakReference.get().getPhaseDao().queryBuilder().where(PhaseDao.Properties
                        .EventId.eq(daoEvent.getId())).buildDelete().executeDeleteWithoutDetachingEntities();

                Rule daoRule;
                if (event.getPhases() != null && event.getPhases().size() > 0) {
                    com.app.iw.model.Phase daoPhase;
                    for (Map.Entry<String, Phase> pme : event.getPhases().entrySet()) {
                        Phase phase = pme.getValue();
                        daoPhase = new com.app.iw.model.Phase();
                        daoPhase.setStartDate(phase.getStartDate());
                        daoPhase.setName(phase.getName());
                        daoPhase.setSortOrder(phase.getSortOrder());
                        daoPhase.setLeaderboardId(phase.getLeaderboardId());
                        daoPhase.setEventId(daoEvent.getId());
                        daoSessionWeakReference.get().getPhaseDao().insert(daoPhase);
                        if (phase.getRules() != null && phase.getRules().size() > 0) {
                            for (Map.Entry<String, String> rme : phase.getRules().entrySet()) {
                                daoRule = new Rule();
                                daoRule.setRule(rme.getValue());
                                daoRule.setPhaseId(daoPhase.getId());
                                daoRule.setEventId(daoEvent.getId());
                                daoSessionWeakReference.get().getRuleDao().insert(daoRule);
                            }
                        }
                    }
                }
                if (event.getRules() != null && event.getRules().size() > 0) {
                    for (Map.Entry<String, String> rme : event.getRules().entrySet()) {
                        daoRule = new Rule();
                        daoRule.setRule(rme.getValue());
                        daoRule.setEventId(daoEvent.getId());
                        daoSessionWeakReference.get().getRuleDao().insert(daoRule);
                    }
                }
        }
        return daoEvent;
    }

    @Override
    protected void onPostExecute(com.app.iw.model.Event event) {
        if (daoOperationCompleteWeakReference.get() != null)
            daoOperationCompleteWeakReference.get().onDaoOperationComplete(event);
    }
}
