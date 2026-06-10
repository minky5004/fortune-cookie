(function () {
    const STORAGE_KEY = 'fortuneCookieDate';
    const STORAGE_MSG = 'fortuneCookieMessage';
    const TODAY       = new Date().toISOString().slice(0, 10);
    const MAX_HITS    = 5;

    const cookieContainer    = document.getElementById('cookieContainer');
    const clickHint          = document.getElementById('clickHint');
    const fortuneResult      = document.getElementById('fortuneResult');
    const fortuneMessage     = document.getElementById('fortuneMessage');
    const alreadyOpened      = document.getElementById('alreadyOpened');
    const savedMessage       = document.getElementById('savedMessage');
    const particlesContainer = document.getElementById('particlesContainer');

    const HINTS = [
        '쿠키를 클릭해서 깨세요!',
        '한 번 더! (4번 남았어요)',
        '조금 더 세게! (3번 남았어요)',
        '거의 다 됐어요! (2번 남았어요)',
        '마지막 한 방이에요!',
    ];

    const CRUMB_COLORS = ['#FAC775', '#EF9F27', '#D4820A', '#B36800', '#633806'];

    let hitCount    = 0;
    let isAnimating = false;

    function createCrumbs(count) {
        for (let i = 0; i < count; i++) {
            const el = document.createElement('div');
            el.className = 'crumb';
            const angle = Math.random() * 360;
            const dist  = 35 + Math.random() * 75;
            const size  = 4 + Math.random() * 9;
            const dur   = 0.45 + Math.random() * 0.35;
            const color = CRUMB_COLORS[Math.floor(Math.random() * CRUMB_COLORS.length)];
            el.style.cssText = `--angle:${angle}deg;--dist:${dist}px;--size:${size}px;--dur:${dur}s;background:${color};`;
            particlesContainer.appendChild(el);
            el.addEventListener('animationend', () => el.remove());
        }
    }

    function triggerShake(level) {
        const name = `shake-${level}`;
        const ms   = 300 + level * 60;
        cookieContainer.style.animation = 'none';
        void cookieContainer.offsetWidth;
        cookieContainer.style.animation = `${name} ${ms}ms ease`;
        setTimeout(() => {
            cookieContainer.style.animation = '';
            isAnimating = false;
        }, ms + 40);
    }

    function showFortune(message) {
        fortuneMessage.textContent = message;
        fortuneResult.classList.add('visible');
        clickHint.style.display = 'none';
    }

    function showAlreadyOpened() {
        cookieContainer.style.display = 'none';
        clickHint.style.display = 'none';
        savedMessage.textContent = localStorage.getItem(STORAGE_MSG) || '';
        alreadyOpened.classList.add('visible');
    }

    if (localStorage.getItem(STORAGE_KEY) === TODAY) {
        showAlreadyOpened();
        return;
    }

    async function breakCookie() {
        cookieContainer.removeEventListener('click', handleClick);
        cookieContainer.classList.add('breaking');

        // 화면 흔들림
        const pageWrapper = document.querySelector('.page-wrapper');
        pageWrapper.classList.add('screen-shake');
        pageWrapper.addEventListener('animationend', () => pageWrapper.classList.remove('screen-shake'), { once: true });

        // 충격 플래시
        const flash = document.createElement('div');
        flash.className = 'impact-flash';
        cookieContainer.appendChild(flash);
        flash.addEventListener('animationend', () => flash.remove(), { once: true });

        // 쿠키 반으로 쪼개기
        const emojiEl = document.getElementById('cookieEmoji');
        const fontSize = getComputedStyle(emojiEl).fontSize;

        const topHalf = document.createElement('span');
        topHalf.className = 'cookie-split-top';
        topHalf.textContent = '🥠';
        topHalf.style.fontSize = fontSize;

        const bottomHalf = document.createElement('span');
        bottomHalf.className = 'cookie-split-bottom';
        bottomHalf.textContent = '🥠';
        bottomHalf.style.fontSize = fontSize;

        cookieContainer.appendChild(topHalf);
        cookieContainer.appendChild(bottomHalf);

        // 가루 대폭발
        createCrumbs(42);

        try {
            const res  = await fetch('/api/fortune');
            if (!res.ok) throw new Error();
            const data = await res.json();
            setTimeout(() => {
                cookieContainer.style.display = 'none';
                localStorage.setItem(STORAGE_KEY, TODAY);
                localStorage.setItem(STORAGE_MSG, data.message);
                showFortune(data.message);
            }, 720);
        } catch {
            cookieContainer.classList.remove('breaking');
            topHalf.remove();
            bottomHalf.remove();
            cookieContainer.addEventListener('click', handleClick);
            alert('운세를 불러오는 데 실패했습니다. 잠시 후 다시 시도해 주세요.');
        }
    }

    function handleClick() {
        if (isAnimating) return;
        isAnimating = true;
        hitCount++;

        // 히트 클래스 갱신
        for (let i = 1; i <= 4; i++) cookieContainer.classList.remove(`hit-${i}`);
        if (hitCount < MAX_HITS) cookieContainer.classList.add(`hit-${hitCount}`);

        if (hitCount >= MAX_HITS) {
            breakCookie();
            return;
        }

        triggerShake(hitCount);
        createCrumbs(4 + hitCount * 2);
        clickHint.textContent = HINTS[hitCount];
    }

    cookieContainer.addEventListener('click', handleClick);
})();
