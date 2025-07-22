
const config = {


}

function openPopup(url,width=500, height=600){
    const left = (window.innerWidth-width)/2;
    const top = (window.innerHeight-height)/2;

    return window.open(url, 'Social Login', `width=${width}, height=${height}, left=${left}, top=${top}`);
}
function initGoogleSignIn(){
    const googleButton = document.querySelector('.social-button[title="Login with Google"]');
    if(googleButton){
        googleButton.addEventListener('click', () => {
            const authUrl = `https://accounts.google.com/o/oauth2/v2/auth?`+
            `client_id=${config.google.clientId}`+
            `&redirect_uri=${encodeURIComponent(config.google.redirectUri)}`+
            `&response_type=code`+
            `&scope=${encodeURIComponent('email profile')}`+
            `&access-type=offline`+
            `&prompt=consent`;
            // console.log('testing');

            const popup = openPopup(authUrl);
            if(popup){
                // window.removeEventListener('message', handleGoogleCallback);
               window.addEventListener('message', handleGoogleCallback)
            }
        })
    }
}

function initFacebookSignIn(){
    const facebookButton = document.querySelector('.social-button[title="Login with Facebook"]');
    if(facebookButton){
        facebookButton.addEventListener('click', ()=> {
            const authUrl = `https://www.facebook.com/v14.0/dialog/oauth?`+
            `client_id=${config.facebook.clientId}`+
            `&redirect_uri=${encodeURIComponent(config.facebook.redirectUri)}`+
            `&response_type=code`
            // `&scope=${encodeURIComponent('email public_profile')}`;

            const popup = openPopup(authUrl);
            if(popup){
                // window.removeEventListener('message', handleFacebookCallback);
                window.addEventListener('message', handleFacebookCallback);
            }
        })
    }
}


function handleGoogleCallback(event){
    // console.log(event.data);


    if(event.data.type === 'google-auth-success'){
        // console.log("Testing log")
        const {code, user} = event.data;
        // console.log(code, user)
        handleSocialLoginSuccess('google', code, user);
    }
}

function handleFacebookCallback(event) {
    // console.log("testing2");

    if(event.origin !== window.location.origin) return;

    if(event.data.type === 'facebook-auth-success'){
        const {code, userData} = event.data;

        handleSocialLoginSuccess('facebook', code, userData);
    }
}
async function handleSocialLoginSuccess(provider, code, userData){
    const loadingoverlay = document.getElementById('loading-overlay');
    try {

        if(loadingoverlay){
            loadingoverlay.style.display='flex';
        }
        const requestData = {
            provider : provider,
            code : code,
            userData : {
                email : userData.email,
            }
        }
        const response = await fetch('http://localhost:8080/api/social-auth/login',{
            method:"POST",
            headers : {
                'Content-Type' : 'application/json',
            },
            body : JSON.stringify(requestData)

        })
        if(!response.ok){
            throw new Error('Failed to process Social Login');
        }
        const data = await response.json();

        if(data.user){
            localStorage.setItem('userData', JSON.stringify({
                id:data.user.id,
                email: data.user.email
            }));

            window.location.href='/client/index.html';
        }else{
            throw new Error('No user data receieved');
        }
    } catch (error) {
        console.log("Social login error: ", error);
        showError('Failed to complete social login login please try again');
    }finally{
        if(loadingoverlay){
            loadingoverlay.style.display='none'
        }
    }
}
function showError(message){
    const errorElement = document.getElementById('error-message');
    if(errorElement){
        errorElement.textContent=message;
        errorElement.style.display='block';
        setTimeout(() => {
            errorElement.style.display='none';
        },5000);
    }
}

document.addEventListener('DOMContentLoaded', () =>{
    initGoogleSignIn();
    initFacebookSignIn();
})