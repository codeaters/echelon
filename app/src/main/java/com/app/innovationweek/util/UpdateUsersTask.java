package com.app.innovationweek.util;

import android.os.AsyncTask;

import com.app.innovationweek.callbacks.DaoOperationComplete;
import com.app.innovationweek.model.Team;
import com.app.innovationweek.model.User;
import com.app.innovationweek.model.dao.DaoSession;
import com.app.innovationweek.model.dao.TeamDao;
import com.google.firebase.database.DataSnapshot;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by zeeshan on 3/14/2017.
 */

public class UpdateUsersTask extends AsyncTask<Void, Void, Void> {
    private WeakReference<DaoSession> daoSessionWeakReference;
    private DataSnapshot usersDataSnapshot;
    private WeakReference<DaoOperationComplete> daoOperationCompleteWeakReference;

    public UpdateUsersTask(DaoSession daoSession, DataSnapshot usersDataSnapshot,
                           DaoOperationComplete daoOperationComplete) {
        daoSessionWeakReference = new WeakReference<DaoSession>(daoSession);
        this.usersDataSnapshot = usersDataSnapshot;
        if (daoOperationComplete != null)
            this.daoOperationCompleteWeakReference = new WeakReference<DaoOperationComplete>
                    (daoOperationComplete);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        User user;
        Team team;
        for (DataSnapshot userDs : usersDataSnapshot.getChildren()) {
            String teamName = userDs.child("team").getValue(String.class);
            List<Team> teams = daoSessionWeakReference.get().getTeamDao().queryBuilder().where(TeamDao
                    .Properties.Name.eq(teamName)).build().list();
            if (teams.size() == 0) {
                team = new Team();
                team.setName(teamName);
                daoSessionWeakReference.get().getTeamDao().insert(team);
            } else {
                team = teams.get(0);
            }
            user = new User();
            user.setId(userDs.getKey());
            user.setName(userDs.child("name").getValue(String.class));
            user.setTeam(team);
            daoSessionWeakReference.get().getUserDao().insertOrReplace(user);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (daoOperationCompleteWeakReference != null && daoOperationCompleteWeakReference.get()
                != null)
            daoOperationCompleteWeakReference.get().onDaoOperationComplete();
        super.onPostExecute(aVoid);
    }
}
