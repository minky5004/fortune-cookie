(function () {
    const STORAGE_KEY = 'fortuneCookieDate';
    const STORAGE_MSG  = 'fortuneCookieMessage';
    const TODAY = new Date().toISOString().slice(0, 10);

    const cookieContainer = document.getElementById('cookieContainer');
    const clickHint       = document.getElementById('clickHint');
    const fortuneResult   = document.getElementById('fortuneResult');
    const fortuneMessage  = document.getElementById('fortuneMessage');
    const alreadyOpened   = document.getElementById('alreadyOpened');
    const savedMessage    = document.getElementById('savedMessage');

    function showFortune(message) {
        fortuneMessage.textContent = message;
        fortuneResult.classList.add('visible');
        clickHint.style.display = 'none';
    }

    function showAlreadyOpened() {
        const msg = localStorage.getItem(STORAGE_MSG) || '';
        cookieContainer.classList.add('disabled');
        cookieContainer.style.display = 'none';
        clickHint.style.display = 'none';
        savedMessage.textContent = msg;
        alreadyOpened.classList.add('visible');
    }

    // 페이지 로드 시 오늘 이미 열었는지 확인
    if (localStorage.getItem(STORAGE_KEY) === TODAY) {
        showAlreadyOpened();
        return;
    }

    // 쿠키 클릭 이벤트
    cookieContainer.addEventListener('click', async function handleClick() {
        cookieContainer.removeEventListener('click', handleClick);

        try {
            const res = await fetch('/api/fortune');
            if (!res.ok) throw new Error('서버 오류');
            const data = await res.json();

            cookieContainer.classList.add('open');

            setTimeout(function () {
                localStorage.setItem(STORAGE_KEY, TODAY);
                localStorage.setItem(STORAGE_MSG, data.message);
                showFortune(data.message);
            }, 600);

        } catch (e) {
            cookieContainer.addEventListener('click', handleClick);
            alert('운세를 불러오는 데 실패했습니다. 잠시 후 다시 시도해 주세요.');
        }
    });
})();
