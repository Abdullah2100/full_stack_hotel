import { createSlice, PayloadAction } from "@reduxjs/toolkit";

interface iAuthState{
    token?:string|null,
    refreshToken?:string|null
}

const initialState:iAuthState = {
    token:localStorage.getItem('access_client'),
    refreshToken:localStorage.getItem('reffresh_client')
}

const jwtSlice = createSlice({
    name: 'auth',
    initialState,  // Fix the typo here
    reducers: {
      // Action to set tokens
      setTokens: (state, action: PayloadAction<{ accessToken: string|undefined; refreshToken: string|undefined}>) => {
        state.token = action.payload.accessToken;
        state.refreshToken = action.payload.refreshToken;
        if(action.payload.accessToken!=undefined)
        localStorage.setItem('access_client', action.payload.accessToken);
      if(action.payload.refreshToken!=undefined)
           localStorage.setItem('reffresh_client', action.payload.refreshToken);
      },
    },
  });


  export const { setTokens } = jwtSlice.actions;  // Export the action creator
  export default jwtSlice.reducer;  